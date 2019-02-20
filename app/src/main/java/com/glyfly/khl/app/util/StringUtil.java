package com.glyfly.khl.app.util;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/1.
 */

public class StringUtil {

    /**
     * 判断字符串是否是数字
     * @param str
     *
     * @return true:是 false:不是
     */
    public static boolean isNumeric(String str) {
        String regex = "^[0-9]+(.[0-9]{1,})?$";
        if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(regex);
    }

    /**
     * 判断字符串是否是中文
     * @param str
     *
     * @return true:是 false:不是
     */
    public static boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";
        if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(regex);
    }

    /**
     * 判断字符串是否是中文名字
     * @param str
     *
     * @return true:是 false:不是
     */
    public static boolean isChineseName(String str) {
        String regex = "^[\u4e00-\u9fa5_a-zA-Z]+$";
        if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(regex);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 校验字符串
     * @return true 是空 ， false 不是空
     */
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str.trim()) && str.trim().length() > 0) {
            return false;
        }
        return true;
    }
}
