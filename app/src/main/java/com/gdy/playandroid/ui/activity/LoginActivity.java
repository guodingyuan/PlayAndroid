package com.gdy.playandroid.ui.activity;

import android.app.Dialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.gdy.playandroid.R;
import com.gdy.playandroid.base.BaseMvpActivity;
import com.gdy.playandroid.mvp.bean.LoginData;
import com.gdy.playandroid.mvp.contract.LoginContract;
import com.gdy.playandroid.mvp.presenter.LoginPresenter;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.utils.LogUtil;

import butterknife.BindView;

public class LoginActivity extends BaseMvpActivity<LoginContract.View, LoginContract.Presenter> implements LoginContract.View{

    @BindView(R.id.usernameET)
    EditText usernameET;
    @BindView(R.id.passwordET)
    EditText passwordET;
    @BindView(R.id.loginBT)
    AppCompatButton loginBT;
    private Dialog loadingDialog;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loginByPassword(usernameET.getText().toString(),passwordET.getText().toString());
            }
        });
        loadingDialog = GlobalUtils.getLoadingDialog(this, "登录中...", false);
    }

    @Override
    protected void getData() {
    }

    @Override
    protected LoginContract.Presenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void loginSuccess(LoginData data) {
        LogUtil.loge("输出："+JSON.toJSONString(data));
    }

    @Override
    public void loginFail() {

    }

    @Override
    public void showLoading() {
        super.showLoading();
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loadingDialog.dismiss();
    }
}
