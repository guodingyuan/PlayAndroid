package com.gdy.playandroid.utils.retrofit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;

public class RxJavaUtils {

    public static <T> void dealData(Observable<ResponseBean<T>> observable, BaseObserver<T> observer){
        LifecycleOwner owner= (LifecycleOwner) observer.getView();
        observable.compose(SchedulerUtils.<ResponseBean<T>>ioToMain())
                .as(AutoDispose.<ResponseBean<T>>autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(observer);
    }

}
