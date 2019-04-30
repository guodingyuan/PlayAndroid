package com.gdy.playandroid.base;

import com.gdy.playandroid.utils.Global;
import com.gdy.playandroid.utils.LogUtil;

public abstract class BaseMvpActivity<V extends IView, P extends IPresenter<V>> extends BaseActivity implements IView{


    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void initView() {
        mPresenter = createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V) this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
        LogUtil.logd("Activity销毁");
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
