package com.gdy.playandroid.widget.web;

import android.content.Context;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * 用于解决WebView与SwipeToLoadLayout嵌套而导致的滑动冲突
 */
public class MyWebView extends com.tencent.smtt.sdk.WebView {

    private SwipeToLoadLayout swipetoloadlayout;
    private boolean enableRefresh=true;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeToLoadLayout getSwipetoloadlayout() {
        return swipetoloadlayout;
    }

    public void setSwipetoloadlayout(SwipeToLoadLayout swipetoloadlayout) {
        this.swipetoloadlayout = swipetoloadlayout;
    }

    public boolean isEnableRefresh() {
        return enableRefresh;
    }

    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t == 0 && enableRefresh){
            swipetoloadlayout.setRefreshEnabled(true);
        }else {
            swipetoloadlayout.setRefreshEnabled(false);
        }
    }
}
