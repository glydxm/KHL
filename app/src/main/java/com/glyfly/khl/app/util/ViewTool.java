package com.glyfly.khl.app.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.glyfly.khl.R;
import com.glyfly.khl.app.entity.Tag;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class ViewTool {

    public static void fixListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) +10;
        listView.setLayoutParams(params);
    }

    public static void createTags(List<Tag> tags, LinearLayout layout){
        if (tags == null || layout == null) {
            return;
        }
        try {
            layout.removeAllViews();
            for (int i = 0; i < tags.size(); i++) {
                String text = tags.get(i).text;
                String textColor = tags.get(i).textColor;
                String bgColor = tags.get(i).bgColor;
                if (!TextUtils.isEmpty(text)) {
                    TextView textView = new TextView(layout.getContext());
                    textView.setBackgroundResource(R.drawable.shape_tag_bg);

                    final Drawable wrappedDrawable = DrawableCompat.wrap(textView.getBackground());
                    ColorStateList colorStateList = ColorStateList.valueOf(ParseUtil.parseColor(bgColor, "#3399FF"));
                    if(colorStateList != null) {
                        DrawableCompat.setTintList(wrappedDrawable, colorStateList);
                    }
                    textView.setBackgroundDrawable(wrappedDrawable);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    textView.setTextColor(ParseUtil.parseColor(textColor, "#ffffff"));
                    textView.setPadding(ScreenUtil.dp2Px(4), ScreenUtil.dp2Px(2), ScreenUtil.dp2Px(4), ScreenUtil.dp2Px(2));
                    textView.setText(text);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.rightMargin = ScreenUtil.dp2Px(5);
                    layout.addView(textView, lp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
