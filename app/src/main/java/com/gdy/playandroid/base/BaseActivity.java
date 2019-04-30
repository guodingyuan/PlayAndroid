package com.gdy.playandroid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gdy.playandroid.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    protected ImageView backIV;

    /**
     * 布局文件id
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化 View
     */
    protected abstract void initView();

    /**
     * 开始请求
     */
    protected abstract void getData();

    /**
     * 是否使用 EventBus
     */
    public boolean useEventBus(){
        return false;//默认不使用
    }


    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unbinder = ButterKnife.bind(this); //绑定初始化ButterKnife
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        backIV=findViewById(R.id.backIV);
        setBack();
        initData();
        initView();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    //设置返回键
    public void setBack() {
        if(backIV!=null){
            backIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
