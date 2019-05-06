package com.gdy.playandroid.mvp.contract;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IPresenter;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import java.util.List;

import io.reactivex.Observable;

public interface NavigationContract {

    interface View extends IView {

        void setNavigationData(List<Navigation> data);
    }

    interface Presenter extends IPresenter<View> {

        void getNavigationData();

    }

    interface Model extends IModel {

        Observable<ResponseBean<List<Navigation>>> getNavigationData();

    }
}
