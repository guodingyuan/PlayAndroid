package com.gdy.playandroid.base;

import android.view.View;

import com.gdy.playandroid.utils.Global;
import com.gdy.playandroid.utils.LogUtil;

public abstract class BaseMvpFragment<V extends IView, P extends IPresenter<V>> extends BaseFragment implements IView {

    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void initView(View view) {
        mPresenter = createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
        LogUtil.logd("Fragment销毁");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showDefaultMsg(String msg) {
        Global.showToast(msg);
    }

    @Override
    public void showMsg(String msg) {
        Global.showToast(msg);
    }

    @Override
    public void showError(String errorMsg) {
        Global.showToast(errorMsg);
    }
}
