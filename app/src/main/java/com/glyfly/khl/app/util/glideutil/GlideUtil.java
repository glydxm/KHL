package com.glyfly.khl.app.util.glideutil;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.glyfly.khl.R;
import com.glyfly.khl.app.MApplication;

/**
 * Created by Administrator on 2017/9/4.
 */

public class GlideUtil {

    public static void loadImage(Object imgUrl, ImageView imageView){
        loadImage(imgUrl, imageView, 0);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, int roundValue){
        loadImage(imgUrl, imageView, roundValue, true);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, boolean isCenterCrop){
        loadImage(imgUrl, imageView, -1, 0, 0, 0, isCenterCrop);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, int roundValue, boolean isCenterCrop){
        loadImage(imgUrl, imageView, -1, 0, 0, roundValue, isCenterCrop);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, int imageWidth, int imageHeight){
        loadImage(imgUrl, imageView, -1, imageWidth, imageHeight, 0);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, int defaultId, int imageWidth, int imageHeight, int roundValue){
        loadImage(imgUrl, imageView, defaultId, imageWidth, imageHeight, roundValue, true);
    }

    public static void loadImage(Object imgUrl, ImageView imageView, int defaultId, int imageWidth, int imageHeight, int roundValue, boolean isCenterCrop){

        if (imageView == null) {
            return;
        }
        DrawableTypeRequest drawableTypeRequest = Glide.with(MApplication.Companion.getInstance().getApplicationContext()).load(imgUrl);
        if (isCenterCrop) {
            drawableTypeRequest.centerCrop();
        }

        Object drawableRequestBuilder;
        if (imageWidth > 0 && imageHeight > 0) {
            drawableRequestBuilder = drawableTypeRequest.override(imageWidth, imageHeight);
        } else {
            drawableRequestBuilder = drawableTypeRequest;
        }

        if (roundValue == -1) {
            ((DrawableRequestBuilder) drawableRequestBuilder).transform(new BitmapTransformation[]{new GlideCircleTransform(MApplication.Companion.getInstance().getApplicationContext())});
        } else if (roundValue > 0) {
            if(isCenterCrop){
                ((DrawableRequestBuilder) drawableRequestBuilder).transform(new CenterCrop(imageView.getContext()),new GlideRoundTransform(MApplication.Companion.getInstance().getApplicationContext(), roundValue));
            }else{
                ((DrawableRequestBuilder) drawableRequestBuilder).transform(new BitmapTransformation[]{new GlideRoundTransform(MApplication.Companion.getInstance().getApplicationContext(), roundValue)});
            }
        }

        if (defaultId > -1) {
            ((DrawableRequestBuilder) drawableRequestBuilder).placeholder(defaultId).error(defaultId);
        } else if (defaultId == -1) {
            ((DrawableRequestBuilder) drawableRequestBuilder).placeholder(R.drawable.default_image).error(defaultId);
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ((DrawableRequestBuilder) drawableRequestBuilder).into(new GlideDrawableImageViewTarget(imageView) {
            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                if (drawable instanceof GifDrawable){
                    drawable.start();
                }
            }

            public void onLoadCleared(Drawable placeholder) {
                super.onLoadCleared(placeholder);
            }

            public void onStart() {
            }

            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
            }
        });
    }
}
