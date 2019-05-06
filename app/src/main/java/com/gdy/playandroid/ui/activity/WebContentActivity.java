package com.gdy.playandroid.ui.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.gdy.playandroid.R;
import com.gdy.playandroid.base.BaseActivity;
import com.gdy.playandroid.constants.Constant;
import com.gdy.playandroid.utils.Global;
import com.gdy.playandroid.widget.web.MyWebViewLayout;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

public class WebContentActivity extends BaseActivity {

    @BindView(R.id.myWebViewLayout)
    MyWebViewLayout myWebViewLayout;
    @BindView(R.id.titleTV)
    TextView titleTV;
    private WebView webView;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_web_content;
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(Constant.CONTENT_URL_KEY);
        if (!TextUtils.isEmpty(url)) {
            myWebViewLayout.loadURL(url);
        } else {
            Global.showToast("链接无效");
        }
        webView = myWebViewLayout.getmWebView();
    }

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra(Constant.CONTENT_TITLE_KEY);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
        } else {
            titleTV.setText("详情");
        }
    }

    @Override
    protected void getData() {

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
