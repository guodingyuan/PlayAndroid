package com.gdy.playandroid.mvp.presenter;

import com.gdy.playandroid.base.BasePresenter;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.mvp.contract.ProjectTreeContract;
import com.gdy.playandroid.mvp.model.ProjectTreeModel;
import com.gdy.playandroid.utils.retrofit.BaseObserver;
import com.gdy.playandroid.utils.retrofit.RxJavaUtils;

import java.util.List;

public class ProjectTreePresenter extends BasePresenter<ProjectTreeContract.Model,ProjectTreeContract.View> implements ProjectTreeContract.Presenter {

    @Override
    public ProjectTreeContract.Model createModel() {
        return new ProjectTreeModel();
    }

    @Override
    public void getProjectTree() {
        RxJavaUtils.dealData(mModel.getProjectTree(), new BaseObserver<List<ProjectTree>>(mModel,mView) {
            @Override
            public void onHandleSuccess(List<ProjectTree> data) {
                mView.setProjectTree(data);
            }
        }.setShowLoading(true));
    }


}
