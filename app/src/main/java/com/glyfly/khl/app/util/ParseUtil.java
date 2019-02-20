package com.glyfly.khl.app.util;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ParseUtil {

    public static int parseColor(String color){
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.parseColor("#000000");
    }

    public static int parseColor(String color, String defaultValue){
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return Color.parseColor(defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.parseColor("#000000");
    }

    public static int parseInt(String string){
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    public static int parseInt(String string, int defaultValue){
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long parseLong(String string){
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.MIN_VALUE;
    }

    public static long parseLong(String string, long defaultValue){
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float parseFloat(String string){
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Float.MIN_VALUE;
    }

    public static float parseFloat(String string, float defaultValue){
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static double parseDouble(String string){
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.MIN_VALUE;
    }

    public static double parseDouble(String string, double defaultValue){
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
