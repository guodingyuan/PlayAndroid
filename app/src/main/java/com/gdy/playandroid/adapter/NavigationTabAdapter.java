package com.gdy.playandroid.adapter;

import com.gdy.playandroid.R;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.utils.GlobalUtils;

import java.util.List;

import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;

public class NavigationTabAdapter implements TabAdapter {

    private List<Navigation> list;

    public NavigationTabAdapter(List<Navigation> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ITabView.TabBadge getBadge(int position) {
        return null;
    }

    @Override
    public ITabView.TabIcon getIcon(int position) {
        return null;
    }

    @Override
    public ITabView.TabTitle getTitle(int position) {
        return new ITabView.TabTitle.Builder()
                .setContent(list.get(position).getName())
                .setTextColor(GlobalUtils.getColor(R.color.white),GlobalUtils.getColor(R.color.gray_6))
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }
}
