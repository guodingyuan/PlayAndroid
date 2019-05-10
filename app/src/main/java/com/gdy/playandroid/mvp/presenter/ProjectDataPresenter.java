package com.gdy.playandroid.mvp.presenter;

import com.gdy.playandroid.base.BasePresenter;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.contract.ProjectDataContract;
import com.gdy.playandroid.mvp.model.ProjectDataModel;
import com.gdy.playandroid.utils.retrofit.BaseObserver;
import com.gdy.playandroid.utils.retrofit.RxJavaUtils;

public class ProjectDataPresenter extends BasePresenter<ProjectDataContract.Model,ProjectDataContract.View> implements  ProjectDataContract.Presenter {
    @Override
    public ProjectDataContract.Model createModel() {
        return new ProjectDataModel();
    }

    @Override
    public void getProjectData(int page, int cid) {
        RxJavaUtils.dealData(mModel.getProjectData(page, cid), new BaseObserver<ArticlePage>(mModel,mView) {
            @Override
            public void onHandleSuccess(ArticlePage data) {
                mView.setProjectData(data);
            }
        });
    }
}
