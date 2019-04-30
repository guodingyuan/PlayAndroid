package com.gdy.playandroid.mvp.contract;


import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IPresenter;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.mvp.bean.LoginData;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import io.reactivex.Observable;

public interface LoginContract {

     interface View extends IView{

        void loginSuccess(LoginData data);

         void loginFail();

    }

    interface Presenter extends IPresenter<View>{

        void loginByPassword(String username,String password);

    }

    interface Model extends IModel{

        Observable<ResponseBean<LoginData>> loginByPassword(String username, String password);

    }
}
