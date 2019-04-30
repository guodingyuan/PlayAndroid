package com.gdy.playandroid.base;

import android.arch.lifecycle.LifecycleObserver;

public abstract class BasePresenter<M extends IModel,V extends IView> implements IPresenter<V>,LifecycleObserver {

    protected M mModel;
    protected V mView;

    /**
     * 创建 Model
     */
    public abstract M createModel();

    @Override
    public void attachView(V mView) {
        this.mView = mView;
        mModel = createModel();
        /*if (mView instanceof LifecycleOwner) {
            ((LifecycleOwner) mView).getLifecycle().addObserver(this);//将P层与V层生命周期绑定
            if (mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mView).getLifecycle().addObserver((LifecycleObserver) mModel);//将M层与V层生命周期绑定
            }
        }*/
    }

    @Override
    public void detachView() {
        mModel.onDetach();
        this.mModel = null;
        this.mView = null;
    }

    /*@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        LogUtil.logd("取消P层的观察");
        owner.getLifecycle().removeObserver(this);//取消P层的观察
    }*/
}
