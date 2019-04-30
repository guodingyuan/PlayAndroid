package com.gdy.playandroid.widget.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

import com.gdy.playandroid.utils.AnimatorUtil;


public class BottomBehavior extends CoordinatorLayout.Behavior<View> {

    private float value;
    private boolean isAnimateIng = false;
    private boolean isShow = true;
    private StateListener hideStateListener=new StateListener(){
        @Override
        public void onAnimationStart(View view) {
            super.onAnimationStart(view);
            isShow = false;
        }
    };
    private StateListener showStateListener=new StateListener(){
        @Override
        public void onAnimationStart(View view) {
            super.onAnimationStart(view);
            isShow=true;
        }
    };

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return  axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimateIng && isShow) { // 向下滑动
            if(value==0){
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                int bottomMargin = layoutParams.bottomMargin;
                value = child.getHeight() + bottomMargin;
            }
            AnimatorUtil.translateHide(child,value,hideStateListener);
        } else if ((dyConsumed < 0 || dyUnconsumed < 0) && !isAnimateIng && !isShow) { // 向上滑动
            AnimatorUtil.translateShow(child,showStateListener);
        }
    }

    private class StateListener implements ViewPropertyAnimatorListener {

        @Override
        public void onAnimationStart(View view) {
            isAnimateIng = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            isAnimateIng = false;
        }

        @Override
        public void onAnimationCancel(View view) {
            isAnimateIng = false;
        }
    }

}
