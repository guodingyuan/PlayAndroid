package com.gdy.playandroid.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.gdy.playandroid.R;
import com.gdy.playandroid.widget.dialog.BaseDialog;

public class UpdateVersionUtil {

    public static void checkUpdate(){
        DownloadBuilder localDownloadBuilder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("https://www.baidu.com")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        LogUtil.loge("返回结果："+result);
                        return UIData.create()//fansclub.oss-cn-hangzhou.aliyuncs.com/app/FANSCLUB_2.2.0.apk
                                .setDownloadUrl("http:////apps.gtax.cn/static/apk/shuidao.apk").setContent("- 新增弹幕功能，让沟通更通畅！\n- 新增新用户引导流程，让上手更快速！\n- 新增官方号，试试撩一下运营小姐姐吧！\n- 优化了系统通知的显示")
                                .setTitle("FC社区更新啦~");
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {

                    }
                });
        //自定义版本升级对话框
        localDownloadBuilder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.version_custom_dialog);
                TextView titleTV = baseDialog.findViewById(R.id.tv_title);
                titleTV.setText(versionBundle.getTitle());
                TextView msgTV = baseDialog.findViewById(R.id.tv_msg);
                msgTV.setText(versionBundle.getContent());
                baseDialog.setCancelable(false);//强制更新
                return baseDialog;
            }
        });
        //自定义下载对话框
        localDownloadBuilder.setCustomDownloadingDialogListener(new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout);
                baseDialog.setCancelable(false);//下载过程不允许退出
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText(GlobalUtils.getString(R.string.versionchecklib_progress, progress));
            }
        });

        //强制升级，对于自定义dialog无效
         /*localDownloadBuilder.setForceUpdateListener(new ForceUpdateListener() {
            @Override
            public void onShouldForceUpdate() {

            }
        });*/
        //利用缓存
        //localDownloadBuilder.setNewestVersionCode(1);
        //静默下载+直接安装（不会弹出升级对话框）
        /*localDownloadBuilder.setDirectDownload(true);
        localDownloadBuilder.setShowNotification(false);
        localDownloadBuilder.setShowDownloadingDialog(false);
        localDownloadBuilder.setShowDownloadFailDialog(false);*/
        localDownloadBuilder.setApkName("FANSCLUB");//设置APK的名称
        localDownloadBuilder.executeMission(Global.getContext());
    }

}
