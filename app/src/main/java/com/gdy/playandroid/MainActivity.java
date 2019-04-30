package com.gdy.playandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdy.playandroid.base.BaseActivity;
import com.gdy.playandroid.ui.activity.LoginActivity;
import com.gdy.playandroid.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.loginBT)
    Button loginBT;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.goTopFAB)
    FloatingActionButton goTopFAB;

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
        final HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.containerFL, homeFragment);
        fragmentTransaction.commit();
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        goTopFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.scrollToTop();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
