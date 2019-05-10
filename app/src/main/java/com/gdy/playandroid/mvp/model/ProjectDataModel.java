package com.gdy.playandroid.mvp.model;

import com.gdy.playandroid.base.BaseModel;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.ProjectDataContract;
import com.gdy.playandroid.utils.retrofit.RetrofitClient;

import io.reactivex.Observable;

public class ProjectDataModel extends BaseModel implements ProjectDataContract.Model {
    @Override
    public Observable<ResponseBean<ArticlePage>> getProjectData(int page, int cid) {
        return RetrofitClient.getApiService().getProjectData(page,cid);
    }
}
