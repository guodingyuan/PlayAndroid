package com.gdy.playandroid.base;

import android.arch.lifecycle.LifecycleObserver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseModel implements IModel,LifecycleObserver {

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        if(disposable!=null){
            mCompositeDisposable.add(disposable);
        }
    }

    @Override
    public void onDetach() {
        if(mCompositeDisposable!=null){
            mCompositeDisposable.clear();//保证Activity结束时取消请求
            mCompositeDisposable = null;
        }
    }


   /* @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        LogUtil.logd("取消M层的观察");
        owner.getLifecycle().removeObserver(this);//取消M层的观察
    }*/

}
