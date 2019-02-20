package com.glyfly.khl.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/2/21.
 */
public class ViewPager extends android.support.v4.view.ViewPager {

    private boolean canSlidable = true;

    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否可滑动
     * @param canSlidable 是否可滑动
     */
    public void setCanSlideable(boolean canSlidable){
        this.canSlidable = canSlidable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canSlidable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (canSlidable) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }
}
