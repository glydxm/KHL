package com.glyfly.khl.app.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


/**
 * Created by Administrator on 2017/8/2.
 */

public class OpenRouter {

    private static final String LOGIN = "login";
    private static final String WEB = "web";
    private static final String FOOD = "food";
    private static final String FUN = "fun";
    private static final String FRIEND = "friend";
    private static final String DECORATION = "decoration";
    private static final String STORE = "store";

    public static void toActivity(Context context, String to, Bundle bundle){
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
//        if (LOGIN.equals(to)) {
//            intent.setClass(context, LoginActivity.class);
//        } else if (WEB.equals(to)) {
//            intent.setClass(context, WebActivity.class);
//        } else if (FOOD.equals(to)) {
//            intent.setClass(context, FoodActivity.class);
//        } else if (FUN.equals(to)) {
//            intent.setClass(context, FunActivity.class);
//        } else if (FRIEND.equals(to)) {
//            intent.setClass(context, FriendActivity.class);
//        } else if (DECORATION.equals(to)) {
//            intent.setClass(context, DecorationActivity.class);
//        } else if (STORE.equals(to)) {
//            intent.setClass(context, StoreActivity.class);
//        }
        context.startActivity(intent);
    }

    //The third API
    public static void jump(Context context, String to, Bundle bundle){

        if (TextUtils.isEmpty(to) || context == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtras(bundle == null ? new Bundle() : bundle);
//        if ("翻译".equals(to)) {
//            intent.setClass(context, TranslationActivity.class);
//        } else if ("菜谱".equals(to)) {
//            intent.setClass(context, MenuActivity.class);
//        } else if ("笑话".equals(to)) {
//            intent.setClass(context, JokeActivity.class);
//        } else if ("新闻头条".equals(to)) {
//            intent.setClass(context, NewsActivity.class);
//        }else {
//            return;
//        }
        context.startActivity(intent);
    }
}
