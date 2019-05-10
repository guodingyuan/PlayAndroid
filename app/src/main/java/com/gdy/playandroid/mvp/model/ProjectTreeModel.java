package com.gdy.playandroid.mvp.model;

import com.gdy.playandroid.base.BaseModel;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.ProjectTreeContract;
import com.gdy.playandroid.utils.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;

public class ProjectTreeModel extends BaseModel implements ProjectTreeContract.Model {

    @Override
    public Observable<ResponseBean<List<ProjectTree>>> getProjectTree() {
        return RetrofitClient.getApiService().getProjectTree();
    }

}
