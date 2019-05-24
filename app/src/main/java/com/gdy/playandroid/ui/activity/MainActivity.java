package com.gdy.playandroid.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdy.playandroid.R;
import com.gdy.playandroid.adapter.MainPagerAdapter;
import com.gdy.playandroid.base.BaseActivity;
import com.gdy.playandroid.base.BaseFragment;
import com.gdy.playandroid.ui.fragment.HomeFragment;
import com.gdy.playandroid.ui.fragment.NavigationFragment;
import com.gdy.playandroid.ui.fragment.ProjectFragment;
import com.gdy.playandroid.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.loginBT)
    Button loginBT;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.goTopFAB)
    FloatingActionButton goTopFAB;
    @BindView(R.id.container_pager)
    ViewPager containerPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private List<BaseFragment> mFragments;
    private Integer[] idArray={R.id.navigation_1, R.id.navigation_2,R.id.navigation_3,R.id.navigation_4,R.id.navigation_5};
    private List<Integer> idList=Arrays.asList(idArray);

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleTV.setText("首页");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(1);//升级到 28.0.0后，可用这句代替下面这句，Nice!
        //BottomNavigationViewHelper.disableShiftMode(navigation);
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new NavigationFragment());
        mFragments.add(new ProjectFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        MainPagerAdapter mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
        containerPager.setAdapter(mAdapter);
        containerPager.setOffscreenPageLimit(4);//防止销毁，使用预加载4个，因fragment使用了懒加载，所以可以这么操作
        containerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navigation.setSelectedItemId(idList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });






        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        goTopFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment baseFragment = mFragments.get(containerPager.getCurrentItem());
                if(baseFragment instanceof NavigationFragment){
                    ((NavigationFragment) baseFragment).scrollToTop();
                }else if(baseFragment instanceof HomeFragment){
                    ((HomeFragment)baseFragment).scrollToTop();
                }else if(baseFragment instanceof ProjectFragment){
                    ((ProjectFragment)baseFragment).scrollToTop();
                }
            }
        });
    }

    @Override
    protected void getData() {

    }

    @Override
    public void setBack() {
        backIV.setVisibility(View.GONE);
    }

    private long exitTime;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-exitTime<2000){
            super.onBackPressed();
        }else {
            exitTime=System.currentTimeMillis();
            Global.showToast("再按一次退出程序");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            containerPager.setCurrentItem(idList.indexOf(i),false);
            titleTV.setText(item.getTitle());
            return true;
        }
    };
}
