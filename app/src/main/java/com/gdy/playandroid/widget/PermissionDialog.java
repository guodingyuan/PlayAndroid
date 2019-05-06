package com.gdy.playandroid.widget;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gdy.playandroid.R;
import com.gdy.playandroid.utils.permission.OnPermisionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionDialog extends DialogFragment {


    @BindView(R.id.msgTV)
    TextView msgTV;
    Unbinder unbinder;
    @BindView(R.id.cancelTV)
    TextView cancelTV;
    @BindView(R.id.submitTV)
    TextView submitTV;
    private String msg;
    private int type=0;//O代表未勾选不再提醒，1代表已勾选不再提醒


    private OnPermisionListener mOnPermisionListener;

    public PermissionDialog setOnPermisionListener(OnPermisionListener onPermisionListener) {
        mOnPermisionListener = onPermisionListener;
        return this;
    }

    public PermissionDialog() {

    }

    public PermissionDialog setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public PermissionDialog setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_permission_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        msgTV.setText(msg);
        if(type==1){
            cancelTV.setText("拒绝");
            submitTV.setText("去设置");
        }else {
            cancelTV.setText("取消");
            submitTV.setText("确定");
        }
        setCancelable(false);//不允许关闭
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cancelFL, R.id.submitFL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelFL:
                if(mOnPermisionListener !=null){
                    mOnPermisionListener.onCancel();
                    dismiss();
                }
                break;
            case R.id.submitFL:
                if(mOnPermisionListener !=null){
                    if(type==0)
                        mOnPermisionListener.onConfirm();
                    else
                        mOnPermisionListener.gotoSetting();
                    dismiss();
                }
                break;
        }
    }
}
