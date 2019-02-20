package com.glyfly.khl.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.glyfly.khl.app.util.ScreenUtil;


/**
 * Created by Administrator on 2018/4/20.
 */

public class StatusBarView extends LinearLayout {

    private View statusHeight;

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public StatusBarView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        statusHeight = new View(context);
        addView(statusHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        statusHeight.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        statusHeight.getLayoutParams().height = ScreenUtil.dp2Px(24);
        if (ScreenUtil.getStatusBarHeight() > 0)
            statusHeight.getLayoutParams().height = ScreenUtil.getStatusBarHeight();
    }
}
