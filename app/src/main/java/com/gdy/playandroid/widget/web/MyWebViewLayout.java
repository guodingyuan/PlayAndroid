package com.gdy.playandroid.widget.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.gdy.playandroid.R;
import com.gdy.playandroid.utils.Global;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.URLUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 *  包含WebView的布局，解决网络错误信息问题及常规设置
 */
public class MyWebViewLayout extends LinearLayout {

    private SwipeToLoadLayout swipetoloadlayout;
    private View dialogLayout;
    private MyWebView mWebView;
    private LinearLayout errorLL;
    private boolean isReLoading;

    public MyWebViewLayout(Context context) {
        super(context);
        init(context);
    }

    public MyWebViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_webview,this,true);
        dialogLayout=view.findViewById(R.id.dialogLayout);
        swipetoloadlayout =  view.findViewById(R.id.swipetoloadlayout);
        mWebView = view.findViewById(R.id.webView);
        errorLL = view.findViewById(R.id.errorLL);
        mWebView.setSwipetoloadlayout(swipetoloadlayout);
        errorLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
                dialogLayout.setVisibility(View.VISIBLE);
                errorLL.setVisibility(View.GONE);
                isReLoading = true;
            }
        });
        GlobalUtils.setWebView(mWebView, client, chromeClient);
        swipetoloadlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                Global.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipetoloadlayout.setRefreshing(false);
                    }
                },500);
            }
        });
        //长按处理各种类型的事件
        mWebView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = mWebView.getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        String url = result.getExtra();
                        if (url != null && URLUtil.isValidUrl(url)) {
                            Uri uri=Uri.parse(url);
                           /* Intent intent=new Intent(context,PhotoZoomActivity.class);
                            intent.putExtra("PhotoUri",uri);
                            context.startActivity(intent);*/
                        }
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;
                }
                return false;
            }
        });
    }


    /**定义一个接口用于拦截请求*/
    public interface InterceptRequest{
        WebResourceResponse onIntercept(String url);
    }
    private InterceptRequest interceptRequest;
    public void setInterceptRequest(InterceptRequest interceptRequest) {
        this.interceptRequest = interceptRequest;
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            boolean isError = checkIsError(title);
            mWebView.setVisibility(isError ? View.GONE : View.VISIBLE);
            errorLL.setVisibility(isError ? View.VISIBLE : View.GONE);
            isReLoading=!isError;
        }
    };
    private WebViewClient client = new WebViewClient() {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
            if(interceptRequest!=null){
                WebResourceResponse webResourceResponse = interceptRequest.onIntercept(s);
                if(webResourceResponse!=null){
                    return webResourceResponse;
                }
            }
            return super.shouldInterceptRequest(webView, s);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialogLayout.setVisibility(View.GONE);
            if (isReLoading) {
                isReLoading = false;
                mWebView.setVisibility(View.VISIBLE);
                errorLL.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError
                error) {
            super.onReceivedError(view, request, error);
            LogUtil.loge("哈哈哈："+error.toString());
            dialogLayout.setVisibility(View.GONE);
            isReLoading = false;
            mWebView.setVisibility(View.GONE);
            errorLL.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dialogLayout.setVisibility(View.VISIBLE);
        }
    };

    private boolean checkIsError(String title) {
        return /*!NetworkUtil.getInstance(getContext()).isNetworkConnected()||*/TextUtils.isEmpty(title)|| title.toLowerCase().contains("error")||title.contains("找不到网页") || title.contains("网页无法打开") || title.contains("服务器运行异常");
        //return title.toLowerCase().contains("error") || title.contains("400") || title.contains("404") || title.contains("401") || title.contains("500") || title.contains("找不到网页") || title.contains("网页无法打开") || (title.contains("000") && title.contains("服务器运行异常"));
    }

    //设置WebView加载的页面
    public void loadURL(String url){
        mWebView.loadUrl(url);
    }

    public com.tencent.smtt.sdk.WebView getmWebView() {
        return mWebView;
    }

    //是否允许页面下拉刷新
    public void setRefreshEnabled(boolean enable){
        swipetoloadlayout.setRefreshEnabled(enable);
        mWebView.setEnableRefresh(enable);
    }
}
