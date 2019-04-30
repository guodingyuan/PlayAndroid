package com.gdy.playandroid.mvp.contract;

import com.gdy.playandroid.base.IModel;
import com.gdy.playandroid.base.IPresenter;
import com.gdy.playandroid.base.IView;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.BannerData;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import java.util.List;

import io.reactivex.Observable;

public interface HomeContract {

    interface View extends IView{
        void setBannerData(List<BannerData> list);

        void setArticleData(ArticlePage articlePage);
    }

    interface Presenter extends IPresenter<View> {
        void requestBannerData();

        void requestArticleData();

        void requestArticleByPage(int pageNum);
    }

    interface Model extends IModel {
        Observable<ResponseBean<List<BannerData>>> requestBannerData();

        Observable<ResponseBean<List<Article>>> requestTopArticles();

        Observable<ResponseBean<ArticlePage>> requestArticles(int pageNum);
    }

}
