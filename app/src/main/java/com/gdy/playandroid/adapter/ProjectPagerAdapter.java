package com.gdy.playandroid.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;

import com.gdy.playandroid.constants.Constant;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.ui.fragment.ProjectDataFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectPagerAdapter extends FragmentStatePagerAdapter {

    private List<ProjectTree> list;
    private List<ProjectDataFragment> fragmentList;

    public ProjectPagerAdapter(FragmentManager fm, List<ProjectTree> list) {
        super(fm);
        this.list=list;
        fragmentList=new ArrayList<>();
        for(ProjectTree projectTree:list){
            ProjectDataFragment projectDataFragment=new ProjectDataFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(Constant.ARGUMENTS_ID,projectTree.getId());
            projectDataFragment.setArguments(bundle);
            fragmentList.add(projectDataFragment);
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return  Html.fromHtml(list.get(position).getName());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public List<ProjectDataFragment> getFragmentList() {
        return fragmentList;
    }
}
