package com.gdy.playandroid.utils.glide;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.AbsListView;
import android.widget.ListView;

public class RecyclerToStaggeredGridScrollListener extends RecyclerView.OnScrollListener {
    public static final int UNKNOWN_SCROLL_STATE = Integer.MIN_VALUE;
    private final AbsListView.OnScrollListener scrollListener;
    private int lastFirstVisible = -1;
    private int lastVisibleCount = -1;
    private int lastItemCount = -1;

    public RecyclerToStaggeredGridScrollListener(@NonNull AbsListView.OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        int listViewState;
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                listViewState = ListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                listViewState = ListView.OnScrollListener.SCROLL_STATE_IDLE;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                listViewState = ListView.OnScrollListener.SCROLL_STATE_FLING;
                break;
            default:
                listViewState = UNKNOWN_SCROLL_STATE;
        }

        scrollListener.onScrollStateChanged(null /*view*/, listViewState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

        int firstVisible = layoutManager.findFirstVisibleItemPositions(null)[0];
        int visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPositions(null)[0]);
        int itemCount = recyclerView.getAdapter().getItemCount();

        if (firstVisible != lastFirstVisible || visibleCount != lastVisibleCount
                || itemCount != lastItemCount) {
            scrollListener.onScroll(null, firstVisible, visibleCount, itemCount);
            lastFirstVisible = firstVisible;
            lastVisibleCount = visibleCount;
            lastItemCount = itemCount;
        }
    }
}