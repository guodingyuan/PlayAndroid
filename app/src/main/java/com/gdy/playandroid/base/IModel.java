package com.gdy.playandroid.base;

import io.reactivex.disposables.Disposable;

public interface IModel {

    void addDisposable(Disposable disposable);

    void onDetach();

}
