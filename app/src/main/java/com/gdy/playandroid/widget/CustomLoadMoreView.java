package com.gdy.playandroid.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.gdy.playandroid.R;


/**
 * 底部加载更多布局
 */
public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_custom_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
