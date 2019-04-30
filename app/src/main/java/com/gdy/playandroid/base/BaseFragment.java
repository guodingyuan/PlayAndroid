package com.gdy.playandroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    /**
     * 视图是否加载完毕
     */
    private boolean isViewPrepare = false;
    /**
     * 数据是否加载过了
     */
    private boolean hasLoadData = false;

    /**
     * 加载布局
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化 View
     */
    protected abstract void initView(View view);

    /**
     * 懒加载
     */
    protected abstract void lazyLoad();

    /**
     * 是否使用 EventBus
     */
    public boolean useEventBus(){
        return false;////默认不使用
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(attachLayoutRes(), null);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder=ButterKnife.bind(this,view);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        isViewPrepare = true;
        initView(view);
        lazyLoadDataIfPrepared();
    }

    private void lazyLoadDataIfPrepared() {
        if (getUserVisibleHint() && isViewPrepare && !hasLoadData) {
            lazyLoad();
            hasLoadData = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
