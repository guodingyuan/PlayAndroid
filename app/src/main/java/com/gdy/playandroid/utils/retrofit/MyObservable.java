package com.gdy.playandroid.utils.retrofit;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.constants.ResponseStatus;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.utils.LogUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class MyObservable<T> extends Observable<ResponseBean<T>> {

    private boolean isShowLoading;

    public MyObservable<T> setShowLoading(boolean showLoading) {
        isShowLoading = showLoading;
        return this;
    }

    public void ss(final IModel model, final IView view, final HandleResponse<T> handleResponse){
        this.compose(SchedulerUtils.<ResponseBean<T>>ioToMain())
                .subscribe(new Observer<ResponseBean<T>>() {
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
                            handleResponse.onHandleSuccess(responseBean.getData());
                        }else {
                            view.showError(responseBean.getErrorMsg());
                            handleResponse.onHandleError(responseBean.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.showError("操作失败，请稍后再试");
                        handleResponse.onHandleError(-1);
                        LogUtil.loge("error:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.loge( "onComplete");
                    }
                });
    }
}
