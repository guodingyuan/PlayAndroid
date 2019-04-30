package com.gdy.playandroid.mvp.model;

import com.gdy.playandroid.base.BaseModel;
import com.gdy.playandroid.mvp.bean.LoginData;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.LoginContract;
import com.gdy.playandroid.utils.retrofit.RetrofitClient;

import io.reactivex.Observable;

public class LoginModel extends BaseModel implements LoginContract.Model {
    @Override
    public Observable<ResponseBean<LoginData>> loginByPassword(String username, String password) {
        return RetrofitClient.getApiService().loginByPassword(username,password);
    }
}
