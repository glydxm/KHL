package com.glyfly.khl.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.glyfly.khl.app.view.MDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class PermissionsUtil {

    public final static int CODE_REQUEST_PERMISSIONS = 20000;
    public static final int PERMISSION_REQUEST_CODE = 100;
    public interface PermissionListener{
        void onGranted();
        void onDenied(List<String> deniedPermissions);
    }

    /**
     * 请求权限
     * @param mActivity
     * @param permissions
     */
    public static void requestPermissions(Activity mActivity, String[] permissions, PermissionListener mListener){
        List<String> permissionLists = new ArrayList<>();
        for(String permission : permissions){
            if(!selfPermissionGranted(mActivity, permission)){
                permissionLists.add(permission);
            }
        }
        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(mActivity, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUEST_CODE);
        }else{
            //表示全都授权了
            mListener.onGranted();
        }
    }

    /**
     * 请求权限结果，baseActivity调用
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param mListener
     */
    public static void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, PermissionListener mListener) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    //存放没授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0; i < grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    /***
     *检查权限
     *
     */
    public static boolean selfPermissionGranted(Context context, String permission) {
        // Android 6.0 以前，全部默认授权
        boolean result = true;
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (targetSdkVersion >= Build.VERSION_CODES.M) {
            // targetSdkVersion >= 23, 使用Context#checkSelfPermission
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
        } else {
            // targetSdkVersion < 23, 需要使用 PermissionChecker
            result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
        }
        return result;
    }

    public static void showPermissionError(final Context context) {
        MDialog.Builder.Companion.getInstance()
                .setMessage("扫一扫无法使用您的相机功能，请前往设置里检查是否开启相机权限")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toCustomSettings(context);
                    }
                }).create(context).show();
    }

    private static void toSetting(Activity mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivityForResult(intent, CODE_REQUEST_PERMISSIONS);
    }

    private static Intent createIntent(Context context){
        Intent intent;
        if (DeviceUtil.isXIAOMI()){
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
        }
        return intent;
    }

    public static void toCustomSettings(Context context){
        try {
            Intent intent = createIntent(context);
            context.startActivity(intent);
        } catch (Exception e) {
            if (context instanceof Activity) {
                toSetting((Activity) context);
            }
            e.printStackTrace();
        }
    }

    public static void toCustomSettingsForResult(Activity context){
        try {
            Intent intent = createIntent(context);
            context.startActivityForResult(intent, CODE_REQUEST_PERMISSIONS);
        } catch (Exception e) {
            toSetting(context);
            e.printStackTrace();
        }
    }
}
