package com.gdy.playandroid.utils.retrofit;

import com.gdy.playandroid.mvp.bean.ResponseBean;

import io.reactivex.Observable;

public class RxJavaUtils {

    public static <T> void dealData(Observable<ResponseBean<T>> observable, BaseObserver<T> observer){
        observable.compose(SchedulerUtils.<ResponseBean<T>>ioToMain())
                .subscribe(observer);
    }

}
