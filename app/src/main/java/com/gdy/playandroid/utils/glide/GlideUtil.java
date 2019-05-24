package com.gdy.playandroid.utils.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gdy.playandroid.R;

/**
 * 封装的图片加载
 */
public class GlideUtil {

    public static void load(String url, ImageView imageView){
        RequestOptions options =new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.color.gray_bg);
        GlideApp.with(imageView)
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(imageView);
    }


}
