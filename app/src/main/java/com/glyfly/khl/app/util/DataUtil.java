package com.glyfly.khl.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.glyfly.khl.app.MApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class DataUtil {

    private static final String TAG = DataUtil.class.getSimpleName();

    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     *
     * @return
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            Log.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 获取SD卡剩余容量（单位Byte）
     *
     * @return
     */
    public static long gainSDFreeSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();

            // 返回SD卡空闲大小
            return freeBlocks * blockSize; // 单位Byte
        } else {
            return 0;
        }
    }

    /**
     * 获取SD卡总容量（单位Byte）
     *
     * @return
     */
    public static long gainSDAllSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 获取所有数据块数
            long allBlocks = sf.getBlockCount();
            // 返回SD卡大小（Byte）
            return allBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取可用的SD卡路径（若SD卡不没有挂载则返回""）
     *
     * @return
     */
    public static String gainSDCardPath() {
        if (isMountedSDCard()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                Log.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     * @param filePath 文件路径
     */
    public static String readFileByLines(String filePath) throws IOException
    {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), System.getProperty("file.encoding")));
            String tempString = null;
            while ((tempString = reader.readLine()) != null)
            {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (reader != null){reader.close();}
        }

        return sb.toString();

    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     * @param filePath 文件路径
     * @param encoding 写文件编码
     */
    public static String readFileByLines(String filePath, String encoding) throws IOException
    {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null)
            {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (reader != null){reader.close();}
        }

        return sb.toString();
    }


    /**
     * 保存内容
     * @param filePath 文件路径
     * @param content 保存的内容
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content) throws IOException
    {
        saveToFile(filePath,content, System.getProperty("file.encoding"));
    }

    /**
     * 指定编码保存内容
     * @param filePath 文件路径
     * @param content 保存的内容
     * @param encoding 写文件编码
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content, String encoding) throws IOException
    {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try
        {
            if(!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));
            writer.write(content);

        } finally
        {
            if (writer != null){writer.close();}
        }
    }

    /**
     * 追加文本
     * @param content 需要追加的内容
     * @param file 待追加文件源
     * @throws IOException
     */
    public static void appendToFile(String content, File file) throws IOException
    {
        appendToFile(content, file, System.getProperty("file.encoding"));
    }

    /**
     * 追加文本
     * @param content 需要追加的内容
     * @param file 待追加文件源
     * @param encoding 文件编码
     * @throws IOException
     */
    public static void appendToFile(String content, File file, String encoding) throws IOException
    {
        BufferedWriter writer = null;
        try
        {
            if(!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
            writer.write(content);
        } finally
        {
            if (writer != null){writer.close();}
        }
    }

    /**
     * 删除文件
     * @param file 要删除的文件
     *
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            } else {
                file.delete();
            }
        } else {
            Toast.makeText(MApplication.Companion.getInstance(), "文件不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @return 是否存在
     * @throws Exception
     */
    public static Boolean isExsit(String filePath)
    {
        Boolean flag = false ;
        try
        {
            File file = new File(filePath);
            if(file.exists())
            {
                flag = true;
            }
        }catch(Exception e){
            System.out.println("判断文件失败-->"+e.getMessage());
        }

        return flag;
    }

    /**
     * 快速读取程序应用包下的文件内容
     *
     * @param context
     *            上下文
     * @param filename
     *            文件名称
     * @return 文件内容
     * @throws IOException
     */
    public static String read(Context context, String filename) throws IOException {
        FileInputStream inStream = context.openFileInput(filename);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param fileName
     *            文件名称
     * @return 文件内容
     * @throws Exception
     */
    public static String read(String fileName) throws IOException {
        FileInputStream inStream = new FileInputStream(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /***
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param fileName
     *            文件名称
     * @param encoding
     *            文件编码
     * @return 字符串内容
     * @throws IOException
     */
    public static String read(String fileName, String encoding)
            throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param rawFileId
     *            raw文件名id
     * @return
     */
    public static String readRawValue(Context context, int rawFileId) {
        String result = "";
        try {
            InputStream is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param fileName
     *            文件名称，包含扩展名
     * @return
     */
    public static String readAssetsValue(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param fileName
     *            文件名称，包含扩展名
     * @return
     */
    public static List<String> readAssetsListValue(Context context, String fileName) {
        List<String> list = new ArrayList<String>();
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, String content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, byte[] content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param modeType
     *            文件写入模式（Context.MODE_PRIVATE、Context.MODE_APPEND、Context.
     *            MODE_WORLD_READABLE、Context.MODE_WORLD_WRITEABLE）
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, byte[] content,
                             int modeType) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    modeType);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定编码将内容写入目标文件
     *
     * @param target
     *            目标文件
     * @param content
     *            文件内容
     * @param encoding
     *            写入文件编码
     * @throws Exception
     */
    public static void write(File target, String content, String encoding)
            throws IOException {
        BufferedWriter writer = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(target, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 指定目录写入文件内容
     * @param filePath 文件路径+文件名
     * @param content 文件内容
     * @throws IOException
     */
    public static void write(String filePath, byte[] content)
            throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 写入文件
     *
     * @param inputStream 下载文件的字节流对象
     * @param filePath 文件的存放路径(带文件名称)
     * @throws IOException
     */
    public static File write(InputStream inputStream, String filePath) throws IOException {
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght = 0 ;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,lenght);
            }
            outputStream.flush();
            return mFile;
        } catch (IOException e) {
            Log.e(TAG, "写入文件失败，原因："+e.getMessage());
            throw e;
        }finally{
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 指定目录写入文件内容
     * @param filePath 文件路径+文件名
     * @param bitmap
     * @throws IOException
     */
    public static void saveAsJPEG(Bitmap bitmap, String filePath)
            throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 指定目录写入文件内容
     * @param filePath 文件路径+文件名
     * @param bitmap
     * @throws IOException
     */
    public static void saveAsPNG(Bitmap bitmap, String filePath)
            throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 序列化对象
     * @param rsls 需要序列化的对象
     * @param filename 文件名
     */
    public static synchronized <T> void serializeObject(T rsls, String filename) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(rsls);
            byte[] data = baos.toByteArray();
            OutputStream os = new FileOutputStream(new File(filename));
            os.write(data);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化对象
     * @param filename 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T deserializeObject(String filename) {
        T obj = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                obj = (T) ois.readObject();
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
