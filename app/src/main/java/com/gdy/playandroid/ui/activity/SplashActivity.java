package com.gdy.playandroid.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.gdy.playandroid.R;
import com.gdy.playandroid.base.BaseActivity;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.utils.permission.PermissionHandler;
import com.gdy.playandroid.utils.permission.PermissionUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.containerFL)
    FrameLayout containerFL;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(2000);
        containerFL.startAnimation(alphaAnimation);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void getData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtil.requestPermission(SplashActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionHandler(true) {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }.setPermissionName(GlobalUtils.getString(R.string.permission_read_phone_state)+"和"+GlobalUtils.getString(R.string.permission_write_external_storage)));
    }
}
