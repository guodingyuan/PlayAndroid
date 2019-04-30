package com.gdy.playandroid.utils.retrofit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

public class MyObservableTransformer<T> implements ObservableTransformer<T, T> {

    private Scheduler subscribeOnScheduler;
    private Scheduler observeOnScheduler;

    public MyObservableTransformer(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler);
    }
}
