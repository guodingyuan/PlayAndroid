package com.gdy.playandroid.mvp.model;

import com.gdy.playandroid.base.BaseModel;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.NavigationContract;
import com.gdy.playandroid.utils.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;

public class NavigationModel extends BaseModel implements NavigationContract.Model {

    @Override
    public Observable<ResponseBean<List<Navigation>>> getNavigationData() {
        return RetrofitClient.getApiService().getNavigationList();
    }
}
