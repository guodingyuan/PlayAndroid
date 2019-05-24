package com.gdy.playandroid.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;


/**
 * @author wyk
 * 初始化全局变量的类
 */

public class Global {
	
	private static Context mContext;						//全局的上下文
	private static Handler mHandler = new Handler();	
	private static Toast mToast;
	
	/** 屏幕宽度 */
	public static int mScreenWidth;
	/** 屏幕高度 */
	public static int mScreenHeight;
	/** 屏幕密度*/
	public static float mDensity;
	

	public static void init(Context context) {
		mContext = context;
		initScreenSize();	
	}
	
	/** 初始化屏幕尺寸参数 */
	private static void initScreenSize() {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mDensity = dm.density;
	}

	/** 提供Handler对象*/
	public static Handler getHandler() {
		return mHandler;
	}
	/** 提供全局的上下文变量*/
	public static Context getContext(){
		return mContext;
	}
	
	
	/** 判断当前是否在主线程运行 */
	public static boolean isInMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}
	
	/** 在主线程运行 */
	public static void runInMainThread(Runnable runnable) {
		if (isInMainThread()) {	// 主线程
			runnable.run();
		} else {				// 子线程
			mHandler.post(runnable);
		}
	}

	/**
	 * 显示toast,可以在子线程调用
	 * @param text	Toast显示的内容
	 */
	public static void showToast(final String text) {
		runInMainThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast=Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
					mToast.setGravity(Gravity.CENTER, 0, 0);
				}
				mToast.setText(text);
				mToast.show();
			}
		});
	}

	/**
	 * 显示toast,可以在子线程调用
	 * @param text	  		  Toast显示的内容
	 * @param duration	  为Toast的显示时长
	 */
	public static void showToast(final String text,final int duration) {
		runInMainThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(mContext, text,duration);
				}
				mToast.setText(text);
				mToast.show();
			}
		});
	}


	/**
	 * 该方法别用，会内存泄漏的！！！！！
	 * 显示toast,可以在子线程调用
	 * @param text	  Toast显示的内容
	 * @param context	  一般为activity的上下文
	 * @param duration	 为Toast的显示时长
	 */
	public static void showToast(final String text, final Context context, final int duration) {
		runInMainThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(context, text,duration);
				}
				mToast.setText(text);
				mToast.show();
			}
		});
	}
}
