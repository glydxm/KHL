package com.glyfly.khl.app.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glyfly.khl.R;


/**
 * Created by Administrator on 2017/3/2.
 */
public class ToolBarUtil {

    private Activity activity;
    private Toolbar toolbar;
    private TextView titleView;
    private String rButton;
    private View base_title;
    private View.OnClickListener listener;
    private boolean showMenu;

    public ToolBarUtil(Activity activity, Toolbar toolbar, View base_title) {
        this.activity = activity;
        this.base_title = base_title;
        this.toolbar = toolbar;
    }

    public boolean isShowMenu(){
        return showMenu;
    }

    public void setMenu(Menu menu){
        if (menu != null && showMenu){
            final MenuItem menuItem = menu.getItem(0);
            menuItem.setVisible(true);
            menuItem.setIcon(null);
            menuItem.setTitle(rButton);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_custom:
                            if (listener != null){
                                listener.onClick(menuItem.getActionView());
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public void initToolBar(String title){
        base_title.setVisibility(View.VISIBLE);
        titleView = new TextView(activity);
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.WHITE);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        toolbar.addView(titleView, params);
        setBackVisible(true);
    }

    public void initToolBar(boolean showBack, String title){
        initToolBar(title);
        setBackVisible(showBack);
    }

    public void setToolBarVisible(boolean showToolBar){

        if (toolbar == null || activity == null){
            return;
        }

        if (showToolBar) {
            base_title.setVisibility(View.VISIBLE);
        } else {
            base_title.setVisibility(View.GONE);
        }
    }

    public void setBackVisible(boolean showToolBar){

        if (toolbar == null || activity == null){
            return;
        }

        if (showToolBar) {
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
    }

    public void setToolBarTitle(String title){

        if (toolbar == null || activity == null){
            return;
        }
        if (titleView == null){
            initToolBar(title);
        }else {
            titleView.setText(title);
        }
    }

    public void initRightButton(String rButton, final View.OnClickListener listener){
        showMenu = true;
        this.rButton = rButton;
        this.listener = listener;
    }
}
