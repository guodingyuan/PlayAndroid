package com.gdy.playandroid.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.gdy.playandroid.utils.Global;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {

	private List<Activity> activitys;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		//不用判断是否在BuildConfig.DEBUG才使用LeakCanary，框架依赖已做了区分处理：debugImplementation/releaseImplementation
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);

		activitys = new ArrayList<>();
		Global.init(this);
		initX5();
	}

	public static MyApplication getInstance() {
		return (MyApplication) Global.getContext();
	}


	public void addActivity(Activity activity){

		if(activitys!=null && ! activitys.contains(activity)){
			activitys.add(activity);
		}
	}

	public void removeActivity(Activity activity){
		if(activitys!=null && activitys.contains(activity)){
			activitys.remove(activity);
		}
	}

	/**退出应用*/
	public void exitApp(){
		try {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		activitys.clear();
	}

	//初始化腾讯X5——文件浏览功能使用
	private void initX5(){
         //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}
}
