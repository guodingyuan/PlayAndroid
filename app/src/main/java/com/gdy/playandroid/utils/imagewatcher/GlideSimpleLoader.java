package com.gdy.playandroid.utils.imagewatcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gdy.playandroid.utils.glide.GlideApp;
import com.github.ielse.imagewatcher.ImageWatcher;

public class GlideSimpleLoader implements ImageWatcher.Loader{
    @SuppressLint("CheckResult")
    @Override
    public void load(Context context, Uri uri, final ImageWatcher.LoadCallback loadCallback) {
        GlideApp.with(context).load(uri).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                loadCallback.onResourceReady(resource);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                loadCallback.onLoadStarted(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                loadCallback.onLoadFailed(errorDrawable);
            }
        });
    }
}
