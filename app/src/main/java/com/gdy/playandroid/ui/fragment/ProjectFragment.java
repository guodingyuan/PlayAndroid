package com.gdy.playandroid.ui.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.classic.common.MultipleStatusView;
import com.gdy.playandroid.R;
import com.gdy.playandroid.adapter.ProjectPagerAdapter;
import com.gdy.playandroid.base.BaseMvpFragment;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.mvp.contract.ProjectTreeContract;
import com.gdy.playandroid.mvp.presenter.ProjectTreePresenter;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends BaseMvpFragment<ProjectTreeContract.View, ProjectTreeContract.Presenter> implements ProjectTreeContract.View {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private ProjectPagerAdapter projectPagerAdapter;

    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_project;
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getProjectTree();
    }


    @Override
    protected ProjectTreeContract.Presenter createPresenter() {
        return new ProjectTreePresenter();
    }

    @Override
    public void setProjectTree(List<ProjectTree> data) {
        if (data != null && data.size() > 0) {
            projectPagerAdapter = new ProjectPagerAdapter(getChildFragmentManager(), data);
            viewPager.setAdapter(projectPagerAdapter);
            viewPager.setOffscreenPageLimit(data.size()-1);
            tablayout.setupWithViewPager(viewPager);
            tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition(), false);//取消viewpager滑动动画
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

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
        projectPagerAdapter.getFragmentList().get(viewPager.getCurrentItem()).scrollToTop();
    }
}
