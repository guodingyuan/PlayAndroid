package com.gdy.playandroid.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.gdy.playandroid.R;
import com.gdy.playandroid.adapter.HomeAdapter;
import com.gdy.playandroid.base.BaseMvpFragment;
import com.gdy.playandroid.constants.Constant;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.BannerData;
import com.gdy.playandroid.mvp.contract.HomeContract;
import com.gdy.playandroid.mvp.presenter.HomePresenter;
import com.gdy.playandroid.ui.activity.WebContentActivity;
import com.gdy.playandroid.utils.glide.GlideImageLoader;
import com.gdy.playandroid.widget.CustomLoadMoreView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseMvpFragment<HomeContract.View, HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipetoloadlayout)
    SwipeToLoadLayout swipetoloadlayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private Banner banner;
    private HomeAdapter adapter;
    private boolean isRefresh;
    private List<Article> articleList;
    private LinearLayoutManager linearLayoutManager;
    private int pageSize;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        swipeTarget.setLayoutManager(linearLayoutManager);
        articleList = new ArrayList<>();
        adapter = new HomeAdapter(articleList);
        View header = getLayoutInflater().inflate(R.layout.home_header, null);
        banner = header.findViewById(R.id.banner);
        adapter.addHeaderView(header);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                int pageNum = adapter.getData().size() / pageSize;//页码从0开始
                mPresenter.requestArticleByPage(pageNum);
            }
        }, swipeTarget);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), WebContentActivity.class);
                intent.putExtra(Constant.CONTENT_URL_KEY, articleList.get(position).getLink());
                intent.putExtra(Constant.CONTENT_TITLE_KEY,articleList.get(position).getTitle());
                startActivity(intent);
            }
        });
        swipeTarget.setAdapter(adapter);
        swipetoloadlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                lazyLoad();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        mPresenter.requestBannerData();
        mPresenter.requestArticleData();

        //UpdateVersionUtil.checkUpdate();//检查版本更新
    }

    @Override
    protected HomeContract.Presenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void setBannerData(final List<BannerData> list) {
        if (list != null && list.size() > 0) {
            List<String> imgList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            for (BannerData bannerData : list) {
                imgList.add(bannerData.getImagePath());
                titleList.add(bannerData.getTitle());
            }
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//风格：显示圆形指示器和标题（水平显示）
            banner.setImageLoader(new GlideImageLoader());//设置图片加载器
            banner.setImages(imgList);
            banner.setBannerTitles(titleList);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent intent = new Intent(getActivity(), WebContentActivity.class);
                    intent.putExtra(Constant.CONTENT_URL_KEY, list.get(position).getUrl());
                    intent.putExtra(Constant.CONTENT_TITLE_KEY,list.get(position).getTitle());
                    startActivity(intent);
                }
            });
        }
        if (isRefresh) {
            swipetoloadlayout.setRefreshing(false);
        }
    }

    @Override
    public void setArticleData(ArticlePage articlePage) {
        if(pageSize==0){
            pageSize=articlePage.getSize();
        }
        List<Article> list = articlePage.getDatas();
        if (isRefresh) {
            swipetoloadlayout.setRefreshing(false);
            adapter.replaceData(list);
        } else {
            adapter.addData(list);
        }
        if (list.size() < pageSize) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
        if(articleList.size()==0){
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        multipleStatusView.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        multipleStatusView.showContent();
    }

    @Override
    public void showError(String errorMsg) {
        super.showError(errorMsg);
        if (isRefresh) {
            swipetoloadlayout.setRefreshing(false);
        }
        adapter.loadMoreFail();
        if(articleList.size()==0){
            multipleStatusView.showError();
        }
    }

    public void scrollToTop(){
        if(linearLayoutManager.findFirstVisibleItemPosition()>20){
            swipeTarget.scrollToPosition(0);
        }else {
            swipeTarget.smoothScrollToPosition(0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(banner!=null){
            if(isVisibleToUser){
                banner.startAutoPlay();
            }else {
                banner.stopAutoPlay();
            }
        }
    }
}
