package com.gdy.playandroid.mvp.contract;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IPresenter;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import java.util.List;

import io.reactivex.Observable;

public interface ProjectTreeContract {

    interface View extends IView {

        void setProjectTree(List<ProjectTree> data);
    }

    interface Presenter extends IPresenter<View> {

        void getProjectTree();
    }

    interface Model extends IModel {

        Observable<ResponseBean<List<ProjectTree>>> getProjectTree();

    }

}
