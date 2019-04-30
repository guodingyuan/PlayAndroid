package com.gdy.playandroid.mvp.presenter;

import com.gdy.playandroid.base.BasePresenter;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.BannerData;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.mvp.contract.HomeContract;
import com.gdy.playandroid.mvp.model.HomeModel;
import com.gdy.playandroid.utils.retrofit.BaseObserver;
import com.gdy.playandroid.utils.retrofit.RxJavaUtils;
import com.gdy.playandroid.utils.retrofit.SchedulerUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class HomePresenter extends BasePresenter<HomeContract.Model,HomeContract.View> implements HomeContract.Presenter {

    @Override
    public HomeContract.Model createModel() {
        return new HomeModel();
    }

    @Override
    public void requestBannerData() {
        RxJavaUtils.dealData(mModel.requestBannerData(),new BaseObserver<List<BannerData>>(mModel,mView) {
            @Override
            public void onHandleSuccess(List<BannerData> data) {
                mView.setBannerData(data);
            }
        });
    }

    @Override
    public void requestArticleData() {
        Observable.zip(mModel.requestTopArticles(), mModel.requestArticles(0),
                new BiFunction<ResponseBean<List<Article>>, ResponseBean<ArticlePage>, ResponseBean<ArticlePage>>() {
                    @Override
                    public ResponseBean<ArticlePage> apply(ResponseBean<List<Article>> listResponseBean, ResponseBean<ArticlePage> articlePageResponseBean) throws Exception {
                        List<Article> articleList = listResponseBean.getData();
                        for (Article article:articleList) {
                            article.setTop(true);
                        }
                        articlePageResponseBean.getData().getDatas().addAll(0,articleList);
                        return articlePageResponseBean;
                    }
                }).compose(SchedulerUtils.<ResponseBean<ArticlePage>>ioToMain())
                .subscribe(new BaseObserver<ArticlePage>(mModel,mView) {
                    @Override
                    public void onHandleSuccess(ArticlePage data) {
                        mView.setArticleData(data);
                    }
                }.setShowLoading(true));
    }

    @Override
    public void requestArticleByPage(int pageNum) {
        RxJavaUtils.dealData(mModel.requestArticles(pageNum), new BaseObserver<ArticlePage>(mModel,mView) {
            @Override
            public void onHandleSuccess(ArticlePage data) {
                mView.setArticleData(data);
            }
        });
    }
}