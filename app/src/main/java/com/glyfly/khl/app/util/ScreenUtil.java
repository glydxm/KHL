package com.glyfly.khl.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.glyfly.khl.app.MApplication;


/**
 * Created by Administrator on 2017/4/1.
 */

public class ScreenUtil {

    /**
     * 获取手机屏幕状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MApplication.Companion.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MApplication.Companion.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机屏幕参数
     */
    public static DisplayMetrics getScreenParams(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取手机屏幕宽度
     */
    public static int getWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) MApplication.Companion.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取手机屏幕高度
     */
    public static int getHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) MApplication.Companion.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     *
     */
    public static int getRealHeight() {
        WindowManager windowManager = (WindowManager) MApplication.Companion.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }

    /**
     * 将dp(dip)转为px
     */
    public static int dp2Px(int dp) {
        float density = MApplication.Companion.getInstance().getResources().getDisplayMetrics().density;
        int px = (int)(density * dp);
        return px;
    }

    /**
     * 将px转为dp(dip)
     */
    public static int pxToDp(int px){
        float density = MApplication.Companion.getInstance().getResources().getDisplayMetrics().density;
        int dp = (int)(px/density);
        return dp;
    }

    private static ViewTreeObserver.OnGlobalLayoutListener layoutListener;
    // 改变键盘弹出时布局移动的位置，解决键盘遮挡输入框和按钮问题
    public static void keywordsSet(final View view_parent, final View view_child) {
        if (layoutListener == null) {
            layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                private int height = 0;

                @Override
                public void onGlobalLayout() {
                    if (view_parent != null
                            && view_parent.getVisibility() == View.VISIBLE) {
                        height = view_parent.getRootView().getHeight();
                        Rect rect = new Rect();
                        view_parent.getWindowVisibleDisplayFrame(rect);
                        if (rect.bottom < height) {
                            int[] location = new int[2];
                            view_child.getLocationOnScreen(location);
                            int scrollHeight = location[1]
                                    + view_child.getHeight() - rect.bottom;
                            view_parent.scrollTo(0, scrollHeight);
                        } else {
                            view_parent.scrollTo(0, 0);
                        }
                    }
                }
            };
        }
        view_parent.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    public static void hideSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromInputMethod(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) {
            return;
        }
        inputMethodManager.showSoftInputFromInputMethod(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 为窗口添加提示蒙层
     *
     * @param activity 需要添加蒙层的Activity
     * @param which 唯一标识一个蒙层，存在本地sp，用于判断是否是第一次进入
     * @param res 蒙层图片
     */
    public static void initVeil(final Activity activity, String which, int res){
        SharedPreferences mySharedPreferences= MApplication.Companion.getInstance().getSharedPreferences("veil", Activity.MODE_PRIVATE);
        boolean tag =mySharedPreferences.getBoolean(which, true);
        if (tag) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean(which, false);
            editor.commit();
            final ImageView veil_home = new ImageView(activity);
            veil_home.setLayoutParams(new WindowManager.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            veil_home.setBackgroundResource(res);
            veil_home.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    activity.getWindowManager().removeView(veil_home);
                }
            });
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            // 设置显示格式
            params.format = PixelFormat.TRANSLUCENT;
            // 设置对齐方式
            params.gravity = Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL;
            // 设置宽高
            params.width = ScreenUtil.getScreenParams(activity).widthPixels;
            params.height = ScreenUtil.getScreenParams(activity).heightPixels-getStatusBarHeight();
            // 添加到当前的窗口上
            activity.getWindowManager().addView(veil_home, params);
        }
    }

    public interface NavigationListener {
        void show();
        void hide();
    }

    //虚拟导航栏显示/隐藏
    public static void setNavigationListener(final View rootView, final NavigationListener navigationListener){
        if (rootView == null || navigationListener == null) {
            return;
        }
        if (getRealHeight() != getHeight()) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                int rootViewHeight;
                @Override
                public void onGlobalLayout() {
                    int viewHeight = rootView.getHeight();
                    if (rootViewHeight != viewHeight) {
                        rootViewHeight = viewHeight;
                        if (viewHeight == getRealHeight()) {
                            //隐藏虚拟按键
                            if (navigationListener != null) {
                                navigationListener.hide();
                            }
                        } else {
                            //显示虚拟按键
                            if (navigationListener != null) {
                                navigationListener.show();
                            }
                        }
                    }
                }
            });
        }
    }
}
