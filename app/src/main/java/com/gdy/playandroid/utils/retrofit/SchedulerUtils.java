package com.gdy.playandroid.utils.retrofit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerUtils {

    public static <T> MyObservableTransformer<T> ioToMain(){
        return new MyObservableTransformer<>(Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
