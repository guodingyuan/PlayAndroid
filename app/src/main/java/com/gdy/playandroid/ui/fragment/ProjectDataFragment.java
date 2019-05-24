package com.gdy.playandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdy.playandroid.R;
import com.gdy.playandroid.adapter.ProjectDataAdapter;
import com.gdy.playandroid.base.BaseMvpFragment;
import com.gdy.playandroid.constants.Constant;
import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.contract.ProjectDataContract;
import com.gdy.playandroid.mvp.presenter.ProjectDataPresenter;
import com.gdy.playandroid.ui.activity.WebContentActivity;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.utils.LogUtil;
import com.gdy.playandroid.utils.glide.GlideApp;
import com.gdy.playandroid.utils.glide.RecyclerStaggeredGridViewPreloader;
import com.gdy.playandroid.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;


public class ProjectDataFragment extends BaseMvpFragment<ProjectDataContract.View, ProjectDataContract.Presenter> implements ProjectDataContract.View {

    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipetoloadlayout)
    SwipeToLoadLayout swipetoloadlayout;

    private ProjectDataAdapter adapter;
    private boolean isRefresh;
    private List<Article> articleList;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int pageSize;

    private int imageWidthPixels;
    private int imageHeightPixels;

    public ProjectDataFragment() {
        // Required empty public constructor
    }

    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(Constant.ARGUMENTS_ID);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//解决item跳动
        swipeTarget.setLayoutManager(staggeredGridLayoutManager);
        articleList = new ArrayList<>();
        adapter = new ProjectDataAdapter(articleList);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                int pageNum = adapter.getData().size() / pageSize + 1;//页码从1开始
                mPresenter.getProjectData(pageNum,id);
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


        //RecyclerView配合Glide预加载图片
        imageWidthPixels=imageHeightPixels=GlobalUtils.dp2px(140);
        ListPreloader.PreloadSizeProvider<String> sizeProvider = new FixedPreloadSizeProvider<>(imageWidthPixels, imageHeightPixels);
        ListPreloader.PreloadModelProvider<String> modelProvider = new MyPreloadModelProvider(getActivity());
        //当布局是LinearLayoutManager用下面这个
        // RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<>(Glide.with(this), modelProvider, sizeProvider, 15);
        RecyclerStaggeredGridViewPreloader<String> preloader=new RecyclerStaggeredGridViewPreloader<>(Glide.with(this), modelProvider, sizeProvider, 15);
        swipeTarget.addOnScrollListener(preloader);


    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_project_data;
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getProjectData(1, id);
    }


    @Override
    protected ProjectDataContract.Presenter createPresenter() {
        return new ProjectDataPresenter();
    }

    @Override
    public void setProjectData(ArticlePage data) {
        if(pageSize==0){
            pageSize=data.getSize();
        }
        List<Article> list = data.getDatas();
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
    }

    public void scrollToTop(){
        int[] firstVisibleItemPositions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
        if(firstVisibleItemPositions[0]>20){
            swipeTarget.scrollToPosition(0);
        }else {
            swipeTarget.smoothScrollToPosition(0);
        }
    }


    private class MyPreloadModelProvider implements ListPreloader.PreloadModelProvider<String> {

        private Context context;

        public MyPreloadModelProvider(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public List<String> getPreloadItems(int position) {

            LogUtil.loge("当前："+position);
            if(position>=articleList.size()){
                LogUtil.loge("大于了");
                return Collections.emptyList();
            }
            return Collections.singletonList(articleList.get(position).getEnvelopePic());
        }

        @Nullable
        @Override
        public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String url) {
            return  GlideApp.with(context)
                    .load(url)
                    .override(imageWidthPixels, imageHeightPixels);
        }
    }
}
