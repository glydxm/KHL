package com.glyfly.khl.app.util;

import android.widget.Toast;

import com.glyfly.khl.app.MApplication;


/**
 * Created by Administrator on 2017/4/12.
 */

public class ToastUtil {

    public static void toast(String msg){
        Toast.makeText(MApplication.Companion.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg, int duration){
        Toast.makeText(MApplication.Companion.getInstance(), msg, duration).show();
    }
}
