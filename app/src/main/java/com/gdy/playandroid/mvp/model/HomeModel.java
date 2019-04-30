package com.gdy.playandroid.mvp.model;

import com.gdy.playandroid.base.BaseModel;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.BannerData;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.HomeContract;
import com.gdy.playandroid.utils.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;

public class HomeModel extends BaseModel implements HomeContract.Model {

    @Override
    public Observable<ResponseBean<List<BannerData>>> requestBannerData() {
        return RetrofitClient.getApiService().requestBannerData();
    }

    @Override
    public Observable<ResponseBean<List<Article>>> requestTopArticles() {
        return RetrofitClient.getApiService().getTopArticles();
    }

    @Override
    public Observable<ResponseBean<ArticlePage>> requestArticles(int pageNum) {
        return RetrofitClient.getApiService().getArticles(pageNum);
    }
}
