package com.gdy.playandroid.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
    private boolean isEndAnim=false;
    private boolean isHasPermission=false;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEndAnim=true;
                gotoMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
                        isHasPermission=true;
                        gotoMain();
                    }
                }.setPermissionName(GlobalUtils.getString(R.string.permission_read_phone_state)+"和"+GlobalUtils.getString(R.string.permission_write_external_storage)));
    }

    private void gotoMain(){
        if(isEndAnim && isHasPermission){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
