package com.glyfly.khl.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/2/21.
 */
public class BitmapUtil {

    public static final int ALL = 347120;
    public static final int TOP = 547120;
    public static final int LEFT = 647120;
    public static final int RIGHT = 747120;
    public static final int BOTTOM = 847120;
    public static final int LEFT_DIAGONAL = 0x123;//左对角线
    public static final int RIGHT_DIAGONAL = 0x223;//右对角线

    /**
     * Bitmap切四个圆角
     *
     * @param bitmap
     * @param topLeftRadius 左上角半径
     * @param topRightRadius 右上角半径
     * @param bottomLeftRadius 左下角半径
     * @param bottomRightRadius 右下角半径
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        try {
            int bigRadius = 1;
            if (bigRadius <= topLeftRadius) {
                bigRadius = topLeftRadius;
            }
            if (bigRadius <= topRightRadius) {
                bigRadius = topRightRadius;
            }
            if (bigRadius <= bottomLeftRadius) {
                bigRadius = bottomLeftRadius;
            }
            if (bigRadius <= bottomRightRadius) {
                bigRadius = bottomRightRadius;
            }

            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            //最大圆角矩形
            RectF rect = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rect, bigRadius, bigRadius, paint);

            //左上角
            if (topLeftRadius < bigRadius) {
                RectF rect1 = new RectF(0, 0, width / 2, height / 2);
                canvas.drawRoundRect(rect1, topLeftRadius, topLeftRadius, paint);
            }
            //右上角
            if (topRightRadius < bigRadius) {
                RectF rect2 = new RectF(width / 2, 0, width, height / 2);
                canvas.drawRoundRect(rect2, topRightRadius, topRightRadius, paint);
            }
            //左下角
            if (bottomLeftRadius < bigRadius) {
                RectF rect3 = new RectF(0, height / 2, width / 2, height);
                canvas.drawRoundRect(rect3, bottomLeftRadius, bottomLeftRadius, paint);
            }
            //右下角
            if (bottomRightRadius < bigRadius) {
                RectF rect4 = new RectF(width / 2, height / 2, width, height);
                canvas.drawRoundRect(rect4, bottomRightRadius, bottomRightRadius, paint);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Rect src = new Rect(0, 0, width, height);
            canvas.drawBitmap(bitmap, src, src, paint);
            return paintingBoard;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 指定图片的切边，对图片进行圆角处理
     *
     * @param type
     * @param bitmap  需要被切圆角的图片
     * @param roundPx 要切的像素大小
     * @return
     */
    public static Bitmap fillet(int type, Bitmap bitmap, int roundPx) {
        try {
            // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
            // 然后在画板上画出一个想要的形状的区域。
            // 最后把源图片帖上。
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            if (TOP == type) {
                clipTop(canvas, paint, roundPx, width, height);
            } else if (LEFT == type) {
                clipLeft(canvas, paint, roundPx, width, height);
            } else if (RIGHT == type) {
                clipRight(canvas, paint, roundPx, width, height);
            } else if (BOTTOM == type) {
                clipBottom(canvas, paint, roundPx, width, height);
            } else if (LEFT_DIAGONAL == type) {
                clipLeftDiagonal(canvas, paint, roundPx, width, height);
            } else if (RIGHT_DIAGONAL == type) {
                clipRightDiagonal(canvas, paint, roundPx, width, height);
            } else {
                clipAll(canvas, paint, roundPx, width, height);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //帖子图
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    private static void clipLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(offset, 0, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, offset * 2, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width - offset, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(width - offset * 2, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipTop(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, offset, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, width, offset * 2);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipBottom(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width, height - offset);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, height - offset * 2, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipAll(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipLeftDiagonal(Canvas canvas, Paint paint, int offset, int width, int height) {
        Rect rect1 = new Rect(offset, 0, width, height - offset);
        canvas.drawRect(rect1, paint);
        Rect rect2 = new Rect(0, offset, width - offset, height);
        canvas.drawRect(rect2, paint);
        canvas.drawCircle(offset, offset, offset, paint);
        canvas.drawCircle(width - offset, height - offset, offset, paint);
    }

    private static void clipRightDiagonal(Canvas canvas, Paint paint, int offset, int width, int height) {
        Rect rect1 = new Rect(0, 0, width - offset, height - offset);
        canvas.drawRect(rect1, paint);
        Rect rect2 = new Rect(offset, offset, width, height);
        canvas.drawRect(rect2, paint);
        canvas.drawCircle(width - offset, offset, offset, paint);
        canvas.drawCircle(offset, height - offset, offset, paint);
    }

    // 质量压缩法：
    private static ByteArrayOutputStream compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        return baos;
    }

    // 图片按比例大小压缩方法（根据路径获取图片并压缩）：
    static ByteArrayOutputStream getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    //图片压缩原图上传
    public static void bitmapToFile(String filePath, Bitmap bitmap)
            throws IOException {
        ByteArrayOutputStream baos = comp(bitmap);
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
    }

    // 图片按比例大小压缩方法（根据Bitmap图片压缩）
    public static ByteArrayOutputStream comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1200f;// 这里设置高度为800f
        float ww = 800f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 回收ImageView的图片
     */
    public static void recycleImageView(View view) {

        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }
}
