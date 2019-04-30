package com.gdy.playandroid.utils;

import android.util.Log;

import com.gdy.playandroid.BuildConfig;


public class LogUtil
{
	private static final boolean LOG_DEBUG = BuildConfig.DEBUG;

	public static void logi(String msg) {
		if (LOG_DEBUG) {
			Log.i("info======>", msg);
		}
	}

	public static void logi(String tag,String msg) {
		if (LOG_DEBUG) {
			Log.i(tag+"======>", msg);
		}
	}

	public static void logd(String msg) {
		if (LOG_DEBUG) {
			Log.d("debug======>", msg);
		}
	}

	public static void loge(String msg) {
		if (LOG_DEBUG) {
			Log.e("error======>", msg);
		}
	}

}
