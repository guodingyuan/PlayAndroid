package com.gdy.playandroid.utils.retrofit;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.constants.ResponseStatus;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<ResponseBean<T>> {

    private IModel model;
    private IView view;
    private boolean isShowLoading;

    public BaseObserver(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    public BaseObserver<T> setShowLoading(boolean showLoading) {
        isShowLoading = showLoading;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isShowLoading) {
            view.showLoading();
        }
        model.addDisposable(d);
    }

    @Override
    public void onNext(ResponseBean<T> responseBean) {
        view.hideLoading();
        if(responseBean.getErrorCode()==ResponseStatus.SUCCESS){
            onHandleSuccess(responseBean.getData());
        }else {
            onHandleError(responseBean.getErrorMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        onHandleError("操作失败，请稍后再试");
        LogUtil.loge("error:" + e.toString());
    }

    @Override
    public void onComplete() {
        LogUtil.loge( "onComplete");
    }



    public abstract void onHandleSuccess(T data);

    public void onHandleError(String errorMsg) {
        view.hideLoading();
        view.showError(errorMsg);
    }
}
