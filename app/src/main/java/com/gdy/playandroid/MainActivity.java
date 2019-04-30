package com.gdy.playandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdy.playandroid.adapter.MainPagerAdapter;
import com.gdy.playandroid.base.BaseActivity;
import com.gdy.playandroid.base.BaseFragment;
import com.gdy.playandroid.ui.activity.LoginActivity;
import com.gdy.playandroid.ui.fragment.HomeFragment;

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
        titleTV.setText("PlayAndroid");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(1);//升级到 28.0.0后，可用这句代替下面这句，Nice!
        //BottomNavigationViewHelper.disableShiftMode(navigation);
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
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
                ((HomeFragment)mFragments.get(0)).scrollToTop();
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            containerPager.setCurrentItem(idList.indexOf(i),false);
            return true;
        }
    };
}
