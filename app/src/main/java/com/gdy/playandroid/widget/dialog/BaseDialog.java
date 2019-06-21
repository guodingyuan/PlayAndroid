package com.gdy.playandroid.widget.dialog;


import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog {

    private int res;
    public BaseDialog(Context context, int theme, int res) {
        super(context, theme);
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }

}
