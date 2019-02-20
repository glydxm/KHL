package okhttp.tool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.glyfly.khl.app.MApplication;
import com.glyfly.khl.app.util.NetworkUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpTool {

	/**
	 * 模拟GET表单（无参数）
	 *
	 * @param url
	 *            请求URL
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.get()//
				.url(url)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Map<String, String> params,
                               Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.get()//
				.url(url)//
				.params(params)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void getNoHeaders(Context context, String url, Map<String, String> params,
                                        Callback<T> callback) {
		OkHttpUtils.get()//
				.url(url)//
				.params(params)//
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Map<String, String> params,
                               Map<String, String> headers, Callback<T> callback) {
		if (headers == null){
			headers = new HashMap<>();
		}
		headers.putAll(getHeader());
		OkHttpUtils.get()//
				.url(url)//
				.params(params)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * post请求（无参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param callback
	 *            回调函数 （可根据自己需求自定义）
	 */
	public static <T> void post(Context context, String url, Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.post()//
				.url(url)//
				.tag(context)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post请求（有参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void post(Context context, String url, Map<String, String> params,
                                Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.post()//
				.url(url)//
				.tag(context)//
				.headers(headers)//
				.params(params)//
				.build()//
				.execute(callback);
	}


	/**
	 * post方式,单个文件上传（无参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param key
	 *            上传文件key
	 * @param file
	 *            上传文件
	 * @param callback
	 *            回调函数
	 */
	public static <T> void updateFile(Context context, String url, String key, File file,
                                      Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.post()//
				.addFile(key, file.getName(), file)//
				.url(url)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post方式,单个文件上传（有参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param keys
	 *            上传文件key数组
	 * @param file
	 *            上传文件
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数
	 */
	public static <T> void updateFile(Context context, String url, String keys, File file,
                                      Map<String, String> params, Callback<T> callback) {
		Map<String, String> headers = getHeader();
		OkHttpUtils.post()//
				.addFile(keys, file.getName(), file)//
				.url(url)//
				.params(params)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post方式,多个文件上传
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param keys
	 *            上传文件key数组
	 * @param files
	 *            上传文件数组
	 * @param callback
	 *            回调函数
	 */

	public static <T> void updateFile(Context context, String url,
                                      String[] keys, File[] files, Callback<T> callback) {
		Map<String, String> headers = getHeader();
		PostFormBuilder Builder = OkHttpUtils.post();
		for (int i = 0; i < files.length; i++) {
			Builder.addFile(keys[i], files[i].getName(), files[i]);
		}
		Builder.url(url)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * 将用户的token放到header
	 *
	 * @return headers 请求header
	 */
	private static Map<String, String> getHeader() {
		Map<String, String> headers = new HashMap<>();

		if (MApplication.user != null && MApplication.user.pin != null) {
			headers.put("pin", MApplication.user.pin);
			headers.put("username", MApplication.user.username);
		}
		if (MApplication.Companion.getInstance().locationEntity != null) {
			headers.put("latitude", MApplication.Companion.getInstance().locationEntity.mLatitude + "");
			headers.put("longitude", MApplication.Companion.getInstance().locationEntity.mLongitude + "");
		}
		headers.put("sys_version", Build.VERSION.RELEASE);//系统版本
		headers.put("sys_type", "Android");//系统类型 Android/IOS
		headers.put("device_type", Build.MODEL);//设备型号
		if (ActivityCompat.checkSelfPermission(MApplication.Companion.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			headers.put("unique_num", ((TelephonyManager) MApplication.Companion.getInstance().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());//唯一标识
		}
		headers.put("app_version", MApplication.Companion.getInstance().getVersionName());//应用当前版本
		headers.put("login_ip", NetworkUtil.getIp());//登陆ip
		headers.put("network_type", NetworkUtil.getNetworkType());//登陆ip

		return headers;
	}
}
