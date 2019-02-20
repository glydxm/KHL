package com.glyfly.khl.app.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.glyfly.khl.R;
import com.glyfly.khl.app.MApplication;
import com.glyfly.khl.app.service.ShakeService;
import com.glyfly.khl.app.util.StatusBarUtil;
import com.glyfly.khl.app.util.ToastUtil;
import com.glyfly.khl.app.util.ToolBarUtil;
import com.glyfly.khl.app.view.MDialog;
import com.glyfly.librarys.eventbus.EventBusEvents;
import com.glyfly.librarys.eventbus.EventBusManager;
import com.tencent.bugly.beta.UpgradeInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.ref.WeakReference;

import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2017/2/21.
 */
public abstract class BaseActivity extends AppCompatActivity implements ShakeService.ShakeListener{

    protected MApplication mApplication;
    protected Activity mContext;
    protected ToolBarUtil toolBarUtil;
    private LinearLayout baseLayout;
    protected Intent intent;

    /** 当前Activity的弱引用，防止内存泄露 **/
    private WeakReference<Activity> context = null;
    protected Service mService;

    public BaseActivity() {
        mContext = this;
    }

    protected abstract int inflateLayout();
    protected abstract void getParams(Bundle bundle);
    protected abstract void initViews(View view);
    protected abstract void doBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 获取应用Application
        mApplication = (MApplication) getApplicationContext();
        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);

        //沉浸式状态栏
        StatusBarUtil.setStatusBarDarkMode(this, true);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        View view = LayoutInflater.from(this).inflate(inflateLayout(), null);
        baseLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        baseLayout.addView(view, params);
        setContentView(baseLayout);

        Toolbar toolbar = (Toolbar) baseLayout.findViewById(R.id.toolbar);
        View titleView = baseLayout.findViewById(R.id.base_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (toolBarUtil == null) {
            toolBarUtil = new ToolBarUtil(this, toolbar, titleView);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            bundle = new Bundle();
        }
        getParams(bundle);

        initViews(view);

        doBusiness();

        intent = new Intent(this, ShakeService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        EventBusManager.getInstance().register(this);
    }

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((ShakeService.MBinder)service).getService();
            ((ShakeService)mService).setShakeListener(BaseActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!toolBarUtil.isShowMenu()){
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolBarUtil.setMenu(menu);
        return true;
    }

    @Subscribe
    public void onEvent(EventBusEvents.CheckVersionEvent checkVersionEvent){
        if (checkVersionEvent != null){
            if (checkVersionEvent.ret == 0) {
                if (checkVersionEvent.strategy instanceof UpgradeInfo){
//                    UpdateServer.showBuglyDialog((UpgradeInfo) checkVersionEvent.strategy, false);
                } else {
                    if (checkVersionEvent.isManual) {
                        ToastUtil.toast("您已是最新版本");
                    }
                }
            } else {
                if (checkVersionEvent.isManual) {
                    ToastUtil.toast("检测新版本失败，请稍后重试");
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mService != null) {
            ((ShakeService)mService).setShakeListener(BaseActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.removeTask(context);
        OkHttpUtils.getInstance().cancelTag(this);
        unbindService(connection);
        EventBusManager.getInstance().unregister(this);
    }

    private Dialog dialog;
    @Override
    public void shaking() {
        if (dialog == null) {
            MDialog.Builder builder = MDialog.Builder.Companion.getInstance();
            builder.setTitle("摇一摇")
                    .setMessage("待更新。。。")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    startActivity(new Intent(mContext, TestActitity.class));
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog = builder.create(mContext);
        }
        dialog.show();
    }
}
