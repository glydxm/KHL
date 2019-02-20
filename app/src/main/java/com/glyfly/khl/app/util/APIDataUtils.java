package com.glyfly.khl.app.util;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp.callback.JSONObjectCallBack;
import okhttp.tool.HttpTool;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/26.
 */

public class APIDataUtils {

    public static final int NEWS = 0;//新闻
    public static final int MENU = 1;//菜谱
    public static final int TRANSLATE = 2;//翻译
    public static final int JOKES = 3;//笑话

    public interface CallBack{
        void onResponse(JSONObject jsonObject, int i);
        void onError(Call call, Exception e, int i);
    }

    private static void requestData(Context context, Map<String, String> params, final CallBack callBack, String url, String appcode){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        HttpTool.get(context, url, params, headers, new JSONObjectCallBack() {

            @Override
            public void onResponse(JSONObject jsonObject, int i) {

                if (callBack != null){
                    callBack.onResponse(jsonObject, i);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
                if (callBack != null){
                    callBack.onError(call, e, i);
                }
            }
        });
    }

    public static void getAPIData(Context context, int type, Map<String, String> params, CallBack callBack){
        switch (type){
            case NEWS:
                getNews(context, params, callBack);
                break;
            case MENU:
                getMenu(context, params, callBack);
                break;
            case TRANSLATE:
                getTranslate(context, params, callBack);
            case JOKES:
                getJokes(context, params, callBack);
                break;
        }
    }

    /**
     * 获取新闻数据
     *  @param context
     *  @param params 接口入参
     */
    private static void getNews(Context context, Map<String, String> params, final CallBack callBack){
        String host = "http://toutiao-ali.juheapi.com";
        String path = "/toutiao/index";
        String appcode = "420ad7c7dde44b33bd31442b6b27c44a";
        requestData(context, params, callBack, host + path, appcode);
    }

    /**
     * 获取菜谱数据
     *  @param context
     *  @param params 接口入参
     */
    private static void getMenu(Context context, Map<String, String> params, final CallBack callBack){
        String host = "http://cook-ali.juheapi.com";
        String path = "/cook/query.php";
        String appcode = "420ad7c7dde44b33bd31442b6b27c44a";
        requestData(context, params, callBack, host + path, appcode);
    }

    /**
     * 获取翻译结果数据
     *  @param context
     *  @param params 接口入参
     */
    private static void getTranslate(Context context, Map<String, String> params, final CallBack callBack){
        String host = "http://jisuzxfy.market.alicloudapi.com";
        String path = "/translate/translate";
        String appcode = "420ad7c7dde44b33bd31442b6b27c44a";
        requestData(context, params, callBack, host + path, appcode);
    }

    /**
     * 获取笑话数据
     *  @param context
     *  @param params 接口入参
     */
    private static void getJokes(Context context, Map<String, String> params, final CallBack callBack){
        String host = "https://ali-joke.showapi.com";
        String path = "/textJoke";
        if (params != null) {
            if ("text".equals(params.get("jokeType"))) {
                path = "/textJoke";
            } else if ("image".equals(params.get("jokeType"))){
                path = "/picJoke";
            } else if ("gif".equals(params.get("jokeType"))){
                path = "/gifJoke";
            }
        }
        String appcode = "420ad7c7dde44b33bd31442b6b27c44a";
        requestData(context, params, callBack, host + path, appcode);
    }
}
