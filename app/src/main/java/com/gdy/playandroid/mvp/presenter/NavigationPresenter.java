package com.gdy.playandroid.mvp.presenter;

import com.gdy.playandroid.base.BasePresenter;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.mvp.contract.NavigationContract;
import com.gdy.playandroid.mvp.model.NavigationModel;
import com.gdy.playandroid.utils.retrofit.BaseObserver;
import com.gdy.playandroid.utils.retrofit.RxJavaUtils;

import java.util.List;

public class NavigationPresenter extends BasePresenter<NavigationContract.Model,NavigationContract.View> implements NavigationContract.Presenter {

    @Override
    public NavigationContract.Model createModel() {
        return new NavigationModel();
    }

    @Override
    public void getNavigationData() {
        RxJavaUtils.dealData(mModel.getNavigationData(), new BaseObserver<List<Navigation>>(mModel,mView) {
            @Override
            public void onHandleSuccess(List<Navigation> data) {
                mView.setNavigationData(data);
            }
        }.setShowLoading(true));
    }
}
