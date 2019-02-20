package com.glyfly.khl.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.glyfly.khl.R;
import com.glyfly.khl.app.util.ToolBarUtil;

/**
 * Created by Administrator on 2017/2/21.
 */
public abstract class BaseFragment extends Fragment {

    private View mContextView;
    protected Activity activity;
    protected ToolBarUtil toolBarUtil;
    private Toolbar toolbar;
    private LinearLayout baseLayout;

    protected abstract int inflateLayout();
    protected abstract void initViews(View view);
    protected abstract void doBusiness(Activity activity);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContextView = inflater.inflate(inflateLayout(), container, false);
        baseLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.activity_base, null);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        baseLayout.addView(mContextView, params);

        toolbar = (Toolbar) baseLayout.findViewById(R.id.toolbar);
        View titleView = baseLayout.findViewById(R.id.base_title);
        toolBarUtil = new ToolBarUtil(activity, toolbar, titleView);

        initViews(mContextView);
        doBusiness(getActivity());
        return baseLayout;
    }
}
