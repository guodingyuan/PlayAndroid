package com.gdy.playandroid.utils.permission;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.gdy.playandroid.R;
import com.gdy.playandroid.utils.Global;
import com.gdy.playandroid.utils.GlobalUtils;
import com.gdy.playandroid.widget.PermissionDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class PermissionUtil {

    @SuppressLint("CheckResult")
    public static void requestPermission(FragmentActivity activity, String[] permissions, PermissionHandler permissionHandler){
        String appName=GlobalUtils.getString(R.string.app_name);
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEachCombined(permissions)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        // All permissions are granted !
                        permissionHandler.onGranted();
                    } else if (permission.shouldShowRequestPermissionRationale){
                    // At least one denied permission without ask never again
                        if (!permissionHandler.onDenied()) {
                            //展示权限选择“拒绝”的对话框
                            new PermissionDialog()
                                    .setMsg("请允许获取“"+permissionHandler.getPermissionName()+"”权限，否则您将无法正常使用"+appName)
                                    .setType(0)
                                    .setOnPermisionListener(new OnPermisionListener() {
                                        @Override
                                        public void gotoSetting() {

                                        }

                                        @Override
                                        public void onConfirm() {
                                            requestPermission(activity,permissions,permissionHandler);//重新请求权限
                                        }

                                        @Override
                                        public void onCancel() {
                                            if(permissionHandler.isForce())
                                                activity.finish();
                                        }
                                    })
                                    .show(activity.getSupportFragmentManager(), "dialog_fragment1");
                        }
                    } else {
                    // At least one denied permission with ask never again
                    // Need to go to the settings
                        if(!permissionHandler.onNeverAsk()){
                            //展示权限选择“不再询问并拒绝”的对话框
                            new PermissionDialog()
                                    .setMsg("由于"+appName+"无法获取“"+permissionHandler.getPermissionName()+"”权限，不能正常工作，请开启权限后再使用。\n"
                                            + "设置路径：设置->应用->"+appName+"->权限")
                                    .setType(1)
                                    .setOnPermisionListener(new OnPermisionListener() {
                                        @Override
                                        public void gotoSetting() {
                                            /**
                                             * 以下方法为调用系统设置中关于本APP的应用详情页
                                             */
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", Global.getContext().getPackageName(), null);
                                            intent.setData(uri);
                                            Global.getContext().startActivity(intent);
                                        }

                                        @Override
                                        public void onConfirm() {

                                        }

                                        @Override
                                        public void onCancel() {
                                            if(permissionHandler.isForce())
                                                activity.finish();
                                        }
                                    })
                                    .show(activity.getSupportFragmentManager(), "dialog_fragment2");
                        }
                    }
                 });
    }


}
