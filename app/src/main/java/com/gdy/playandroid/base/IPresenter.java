package com.gdy.playandroid.base;

public interface IPresenter<V extends IView> {
    /**
     * 绑定 View
     */
    void attachView(V mView);

    /**
     * 解绑 View
     */
    void detachView();
}
