package com.glyfly.khl.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.glyfly.khl.app.MApplication;

public class SharedPreferensesUtil {
    private final static String APPDATA = "app_data";

    public static void putIntValue(String key, int value) {
        Editor sp = MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public static int getIntValue(String key, int defValue) {
        SharedPreferences sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public static void putLongValue(String key, long value) {
        Editor sp = MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.putLong(key, value);
        sp.commit();
    }

    public static long getLongValue(String key, long defValue) {
        SharedPreferences sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE);
        long value = sp.getLong(key, defValue);
        return value;
    }

    public static void putStringValue(String key, String value) {
        Editor sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static String getStringValue(String key, String defValue) {
        SharedPreferences sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }

    public static void putBooleanValue(String key, boolean value) {
        Editor sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public static boolean getBooleanValue(String key, boolean defValue) {
        SharedPreferences sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static void removeValue(String key) {
        Editor sp = MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.remove(key);
        sp.commit();
    }

    public static void clearValue(){
        Editor sp =  MApplication.Companion.getInstance().getSharedPreferences(APPDATA, Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }
}  