package com.gdy.playandroid.mvp.contract;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IPresenter;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import io.reactivex.Observable;

public interface ProjectDataContract {

    interface View extends IView {

        void setProjectData(ArticlePage data);
    }

    interface Presenter extends IPresenter<View> {

        void getProjectData(int page,int cid);
    }

    interface Model extends IModel {

        Observable<ResponseBean<ArticlePage>> getProjectData(int page, int cid);

    }

}
