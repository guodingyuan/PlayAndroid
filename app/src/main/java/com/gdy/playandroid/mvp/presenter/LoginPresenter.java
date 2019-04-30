package com.gdy.playandroid.mvp.presenter;

import com.gdy.playandroid.base.BasePresenter;
import com.gdy.playandroid.mvp.bean.LoginData;
import com.gdy.playandroid.mvp.contract.LoginContract;
import com.gdy.playandroid.mvp.model.LoginModel;
import com.gdy.playandroid.utils.retrofit.BaseObserver;
import com.gdy.playandroid.utils.retrofit.RxJavaUtils;

public class LoginPresenter extends BasePresenter<LoginContract.Model,LoginContract.View> implements LoginContract.Presenter {

    @Override
    public LoginContract.Model createModel() {
        return new LoginModel();
    }

    @Override
    public void loginByPassword(String username, String password) {
        RxJavaUtils.dealData(mModel.loginByPassword(username, password),new BaseObserver<LoginData>(mModel, mView){
            @Override
            public void onHandleSuccess(LoginData data) {
                mView.loginSuccess(data);
            }
        }.setShowLoading(true));
    }
}
