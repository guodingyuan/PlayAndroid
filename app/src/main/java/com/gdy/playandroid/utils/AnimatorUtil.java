package com.gdy.playandroid.utils;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

public class AnimatorUtil {

    /**
     * 显示View
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    public static void  scaleShow(View view, ViewPropertyAnimatorListener listener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(800)
                .setListener(listener)
                .setInterpolator(getInterpolator(0))
                .start();
    }

    /**
     * 隐藏View
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    public static void  scaleHide(View view, ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(800)
                .setInterpolator(getInterpolator(0))
                .setListener(listener)
                .start();
    }

    /**
     * 显示view
     *
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    public static void  translateShow(View view, ViewPropertyAnimatorListener listener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(0f)
                .setDuration(400)
                .setListener(listener)
                .setInterpolator(getInterpolator(0))
                .start();
    }

    /**
     * 隐藏view
     *
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    public static void translateHide(View view,float value,ViewPropertyAnimatorListener listener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(value)
                .setDuration(400)
                .setInterpolator(getInterpolator(0))
                .setListener(listener)
                .start();
    }

    private static LinearOutSlowInInterpolator linearOutSlowInInterpolator;
    private static AccelerateInterpolator accelerateInterpolator;

    private static Interpolator getInterpolator(int type){//0为线性，1为加速
        if(type==0){
            if(linearOutSlowInInterpolator==null){
                linearOutSlowInInterpolator=new LinearOutSlowInInterpolator();
            }
            return linearOutSlowInInterpolator;
        }else if(type==1){
            if(accelerateInterpolator==null){
                accelerateInterpolator=new AccelerateInterpolator();
            }
            return accelerateInterpolator;
        }
        return null;
    }

}
