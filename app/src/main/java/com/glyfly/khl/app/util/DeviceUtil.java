package com.glyfly.khl.app.util;

import android.os.Build;

/**
 * Created by Administrator on 2018/4/20.
 */

public class DeviceUtil {

    public static final String BRAND_HUAWEI = "huawei";//华为
    public static final String BRAND_XIAOMI = "xiaomi";//小米
    public static final String BRAND_MEIZU = "meizu";//魅族
    public static final String BRAND_ZTE = "zte";//中兴
    public static final String BRAND_SAMSUNG = "samsung";//三星
    public static final String BRAND_HONOR = "honor";//荣耀
    public static final String BRAND_OPPO = "oppo";//OPPO
    public static final String BRAND_VIVO = "vivo";//VIVO

    private static String getDeviceBrand(){
        return Build.BRAND;
    }

    /**
     * 检测华为
     *
     * @return
     */
    public static boolean isHUAWEI() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_HUAWEI)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测小米
     *
     * @return
     */
    public static boolean isXIAOMI() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_XIAOMI)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测魅族
     *
     * @return
     */
    public static boolean isMEIZU() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_MEIZU)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测中兴
     *
     * @return
     */
    public static boolean isZTE() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_ZTE)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测三星
     *
     * @return
     */
    public static boolean isSAMSUNG() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_SAMSUNG)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测荣耀
     *
     * @return
     */
    public static boolean isHONOR() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_HONOR)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测oppo
     *
     * @return
     */
    public static boolean isOPPO() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_OPPO)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检测vivo
     *
     * @return
     */
    public static boolean isVIVO() {
        if (getDeviceBrand().toLowerCase().contains(BRAND_VIVO)){
            return true;
        }else {
            return false;
        }
    }
}
