package com.gdy.playandroid.ui.fragment;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.classic.common.MultipleStatusView;
import com.gdy.playandroid.R;
import com.gdy.playandroid.adapter.NavigationRVadapter;
import com.gdy.playandroid.adapter.NavigationTabAdapter;
import com.gdy.playandroid.base.BaseMvpFragment;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.mvp.contract.NavigationContract;
import com.gdy.playandroid.mvp.presenter.NavigationPresenter;

import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseMvpFragment<NavigationContract.View, NavigationContract.Presenter> implements NavigationContract.View {


    @BindView(R.id.tablayout)
    VerticalTabLayout tablayout;
    @BindView(R.id.linkRV)
    RecyclerView linkRV;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linkRV.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getNavigationData();
    }

    @Override
    protected NavigationContract.Presenter createPresenter() {
        return new NavigationPresenter();
    }

    @Override
    public void setNavigationData(List<Navigation> data) {
        if(data!=null && data.size()>0){
            tablayout.setTabAdapter(new NavigationTabAdapter(data));
            linkRV.setAdapter(new NavigationRVadapter(data));
            tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabView tab, int position) {
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    //LogUtil.loge("位置："+position+"，开始："+firstVisibleItemPosition+"，结束："+lastVisibleItemPosition+"，总数："+linearLayoutManager.getItemCount());
                    if(position>=firstVisibleItemPosition && position<=lastVisibleItemPosition && lastVisibleItemPosition!=linearLayoutManager.getItemCount()-1){
                        //LogUtil.loge("距离："+linkRV.getChildAt(position-firstVisibleItemPosition).getTop());
                        linkRV.smoothScrollBy(0,linkRV.getChildAt(position-firstVisibleItemPosition).getTop());
                    }else {
                        linearLayoutManager.scrollToPositionWithOffset(position,0);//定位到指定项如果该项可以置顶就将其置顶显示
                    }
                }

                @Override
                public void onTabReselected(TabView tab, int position) {

                }
            });

            linkRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        tablayout.setTabSelected(firstVisibleItemPosition);
                    }
                }
            });
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
        multipleStatusView.showError();
    }

    public void scrollToTop(){
        tablayout.setTabSelected(0);
    }
}
