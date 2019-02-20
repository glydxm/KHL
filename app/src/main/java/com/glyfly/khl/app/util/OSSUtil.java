package com.glyfly.khl.app.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.glyfly.khl.app.MApplication;
import com.glyfly.librarys.oss.OssService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 123 on 2017/4/20.
 */

public class OSSUtil {

    private OSSClient ossClient;
    private Map<String, OSSAsyncTask> map;

    public interface UpLoadingListener{
        void onProgress(PutObjectRequest request, long currentSize, long totalSize);
        void onSuccess(PutObjectRequest request, PutObjectResult result);
        void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);
    }

    public interface DownLoadingListener{
        void onSuccess(GetObjectRequest request, GetObjectResult result, String sResult);
        void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException);
    }

    private OSSUtil(){
        init();
    }

    private static class OSSUtilHolder{
        private static OSSUtil INSTATNCE = new OSSUtil();
    }

    public static OSSUtil getInstance(){
        return OSSUtilHolder.INSTATNCE;
    }

    private void init(){
        map = new HashMap<>();
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIX0soUpjuK2JJ", "6EneSjqB6VXQhZoBYD2zHQI7VL52jb");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        ossClient = new OSSClient(MApplication.Companion.getInstance(), endpoint, credentialProvider, conf);
    }

    public OssService createOssService(Activity activity){
        return new OssService(activity, ossClient, "highlife-picture", null);
    }

    public void cancleTask(String fileName){
        if (map.get(fileName) != null) {
            map.get(fileName).cancel();
        }
    }

    /**
     * 简单上传本地文件
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param listener 上传文件的回调接口
     *
     */
    public void uploadFile(String fileName, String filePath, final UpLoadingListener listener){

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("highlife-file", fileName, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                listener.onProgress(request, currentSize, totalSize);
            }
        });

        OSSAsyncTask task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                listener.onSuccess(request, result);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                listener.onFailure(request, clientExcepion, serviceException);
            }
        });
        if (!TextUtils.isEmpty(fileName)) {
            map.put(fileName, task);
        }
    }

    public PutObjectResult uploadBytes(String bytesName, byte[] bytes){
        PutObjectResult putResult = null;
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("highlife-data-string", bytesName, bytes);
        try {
            putResult = ossClient.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
        return putResult;
    }

    public void downLoadFile(String fileName, boolean convertToString, final DownLoadingListener listener){
        GetObjectRequest get = new GetObjectRequest("highlife-file", fileName);
        download(fileName, get, convertToString, listener);
    }

    private void download(String fileName, GetObjectRequest get, final boolean convertToString, final DownLoadingListener listener){
        OSSAsyncTask task = ossClient.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                if (convertToString) {
                    InputStream inputStream = result.getObjectContent();
                    byte[] buffer = new byte[2048];
                    StringBuilder builder = new StringBuilder();
                    int len;
                    try {
                        while ((len = inputStream.read(buffer)) != -1) {
                            // 处理下载的数据
                            builder.append(new String(buffer, 0, len));
                        }
                        listener.onSuccess(request, result, builder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onSuccess(request, result, "");
                }
            }
            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                listener.onFailure(request, clientExcepion, serviceException);
            }
        });

        if (!TextUtils.isEmpty(fileName)) {
            map.put(fileName, task);
        }
    }

    public static void loadPicToImageView(Activity activity, String object, final ImageView imageView){
        OssService ossService = getInstance().createOssService(activity);
        ossService.setLoadListener(new OssService.LoadListener() {
            @Override
            public void loadProgress(int progress) {

            }

            @Override
            public void loadComplete(Object object) {
                if (object instanceof Bitmap) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap((Bitmap) object);
                }
            }

            @Override
            public void loadFail(String info) {
            }
        });
        ossService.asyncGetImage(object);
    }
}
