package com.glyfly.khl.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.glyfly.khl.app.MApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 123 on 2017/7/5.
 */

public class FileUtil {

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件名
     *
     */
    public static boolean isFileExist(String filePath) {
        return filePath != null?(new File(filePath)).exists():false;
    }

    /**
     * 检测SD卡是否可用
     *
     * @return true-可用,false-不可用
     */
    public static boolean checkSDCard(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }else {
            return false;
        }
    }

    public static String createAppFilePath(String fileName){
        String filePath = "";
        try {
            filePath = MApplication.Companion.getInstance().getFilesDir().getPath() + File.separator + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static String createAppCachePath(String fileName){
        String filePath = "";
        try {
            filePath = MApplication.Companion.getInstance().getCacheDir().getPath() + File.separator + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 创建文件存取路径
     *
     * @param fileName 文件名称
     * @return 文件存取路径
     */
    public static String createPath(Context context, String fileName){

        if (context == null || TextUtils.isEmpty(fileName)){
            return "";
        }
        String filePath;
        if (checkSDCard()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                filePath = file.getPath() + File.separator + fileName;
            }else {
//                filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;
                filePath = context.getCacheDir().getPath() + File.separator + fileName;
            }
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + fileName;
        }
        return filePath;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = (double)options.outWidth;
        double h = (double)options.outHeight;
        int lowerBound = maxNumOfPixels == -1?1:(int) Math.ceil(Math.sqrt(w * h / (double)maxNumOfPixels));
        int upperBound = minSideLength == -1?128:(int) Math.min(Math.floor(w / (double)minSideLength), Math.floor(h / (double)minSideLength));
        return upperBound < lowerBound?lowerBound:(maxNumOfPixels == -1 && minSideLength == -1?1:(minSideLength == -1?lowerBound:upperBound));
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if(initialSize <= 8) {
            for(roundedSize = 1; roundedSize < initialSize; roundedSize <<= 1) {
                ;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * 保存bitmap到本地文件
     *
     * @param fileName 文件名
     * @param bitmap
     */
    public static void saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {

        if (context == null || bitmap == null || TextUtils.isEmpty(fileName)){
            return;
        }
        try {
            String filePath = createPath(context, fileName);
            if(!TextUtils.isEmpty(filePath)) {
                File f = new File(filePath);
                if(f.exists()) {
                    f.delete();
                }
                if(!f.exists()) {
                    FileOutputStream fOut = null;
                    try {
                        f.createNewFile();
                        fOut = new FileOutputStream(f);
                    } catch (FileNotFoundException var6) {
                        var6.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(fileName.endsWith("png")) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    }
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地文件获取bitmap
     *
     * @param fileName 文件名
     * @param targetSize bitmap最大pixel
     *
     * @return
     */
    public static Bitmap getBitmapFromFile(Context context, String fileName, int targetSize) {

        if (context == null || TextUtils.isEmpty(fileName)){
            return null;
        }
        try {
            String filePath = createPath(context, fileName);
            if(!TextUtils.isEmpty(filePath) && isFileExist(filePath)) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                options.inSampleSize = computeSampleSize(options, -1, targetSize);
                options.inJustDecodeBounds = false;
                Bitmap value = BitmapFactory.decodeFile(filePath, options);
                return value;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return null;
    }

    public static String getStringFromAssets(Context context, String fileName){
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String text = new String(buffer, "utf-8");
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "读取失败";
    }

    /**
     * 将文件从本地文件删除
     *
     * @param fileName 文件名
     */
    public static void deleteFile(Context context, String fileName) {

        if (context == null || TextUtils.isEmpty(fileName)){
            return;
        }
        try {
            String filePath = createPath(context, fileName);
            if(!TextUtils.isEmpty(filePath)) {
                File f = new File(filePath);
                if(f.exists()) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存字符串到本地文件，覆盖原文件
     *
     * @param str 要保存的字符串
     * @param fileName 文件名
     */
    public static void saveStringToFile(Context context, String str, String fileName) {
        saveStringToFile(context, str, fileName, false, false);
    }

    /**
     * 保存字符串到本地文件
     *
     * @param str 要保存的字符串
     * @param fileName 文件名
     * @param append 是否追加写入,true-追加写入,false-覆盖写入
     * @param autoLine 追加写入时是否换行,true-换行,false-不换行
     */
    public static void saveStringToFile(Context context, String str, String fileName, boolean append, boolean autoLine) {

        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(fileName)){
            return;
        }
        String filePath = createPath(context, fileName);
        try {
            File file = new File(filePath);
            if (file.exists()){
                file.delete();
            }
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            if (append) {
                //如果为追加则在原来的基础上继续写文件
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(str.getBytes());
                if (autoLine) {
                    raf.write("\n".getBytes());
                }
                raf.close();
            } else {
                //重写文件，覆盖掉原来的数据
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(str.getBytes());
                outStream.flush();
                outStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地文件获取字符串
     *
     * @param fileName 文件名
     * @return 获取的字符串
     */
    public static String getStringFromFile(Context context, String fileName) {

        if (context == null || TextUtils.isEmpty(fileName)){
            return "";
        }
        String filePath = createPath(context, fileName);
        String str = "";
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 同步下载文件到本地
     * @param url 文件地址
     * @param fileName 本地保存的文件名
     *
     * @return 保存的文件
     */
    public static File downloadFile(String url, String fileName){
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        try {
            // 构造URL
            URL remoteUrl = new URL(new URI(url).toASCIIString());
            // 打开连接
            URLConnection con = remoteUrl.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :"+contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(fileName);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            File newFile = new File(fileName);
            if (newFile.exists()) {
                return newFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步下载文件到本地
     * @param url 文件地址
     * @param fileName 本地保存的文件名
     * @param listner 下载监听
     *
     * @return 保存的文件
     */
    public static void downloadFile(String url, String fileName, DownloadListner listner){
        new DownloadTask(url, fileName, listner).execute();
    }

    /** 文件下载异步任务 */
    private static class DownloadTask extends AsyncTask<Void, Void, File> {

        private String url;
        private String fileName;
        private DownloadListner listner;

        public DownloadTask(String url, String fileName, DownloadListner listner) {
            this.url = url;
            this.fileName = fileName;
            this.listner = listner;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listner.onStart();
        }

        @Override
        protected File doInBackground(Void... params) {
            File file = new File(fileName);
            if(file.exists()){
                file.delete();
            }
            try {
                // 构造URL
                URL remoteUrl = new URL(new URI(url).toASCIIString());
                // 打开连接
                URLConnection con = remoteUrl.openConnection();
                //获得文件的长度
                int contentLength = con.getContentLength();
                // 输入流
                InputStream is = con.getInputStream();
                // 4K的数据缓冲
                byte[] bs = new byte[1024*4];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                OutputStream os = new FileOutputStream(fileName);
                // 开始读取
                int downloaded = 0;
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                    downloaded += len;
                    listner.onProgress(downloaded, contentLength);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                if (downloaded == contentLength) {
                    listner.onComplete(file);
                    return file;
                } else {
                    listner.onError("loadedSize != totalSize");
                }
            } catch (Exception e) {
                e.printStackTrace();
                listner.onError(e.toString());
            }
            return null;
        }
    }

    /** 文件下载监听 */
    public interface DownloadListner{
        void onStart();
        void onProgress(int downloaded, int total);
        void onComplete(File file);
        void onError(String error);
    }
}
