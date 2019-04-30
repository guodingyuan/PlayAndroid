package com.gdy.playandroid.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gdy.playandroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author wyk
 *
 */
public class GlobalUtils {

	/** dp转px*/
	public static int dp2px(int dp) {
		float density = Global.mDensity;
		return (int) (density * dp + 0.5f);
	}
	
	/** inflate布局*/
	public static View inflate(int layoutRes, ViewGroup parent) {
		return LayoutInflater.from(Global.getContext()).inflate(layoutRes, parent, false);
	}
	
	/** 获取Resources*/
	public static Resources getResources(){
		return Global.getContext().getResources();
	}
	/** 根据id获取图片的Drawable*/
	public static Drawable getDrawable(int drawId){
		return getResources().getDrawable(drawId);
	}
	/** 根据id获取字符串*/
	public static String getString(int strId){
		return getResources().getString(strId);
	}
	/** 根据id获取字符数组*/
	public static String[] getStringArray(int arrId){
		return getResources().getStringArray(arrId);
	}

	/** inflate布局,使用Activity的上下文*/
	public static View inflate(Context context,int layoutRes, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(layoutRes, parent, false);
	}


	//显示键盘
	public static void showKeyboard(final EditText editText){
		Global.runInMainThread(new Runnable() {
			@Override
			public void run() {
				editText.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) editText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		});
	}

	/** 隐藏键盘*/
	public static void hideKeyboard(View view){
		Context context = view.getContext();
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	  }

	/** 防止点击多次的判断*/
	private static long lastClickTime;
	private final static long EXIT_GAP = 500;
	public static boolean isValidClick(){
		if(SystemClock.uptimeMillis() - lastClickTime > EXIT_GAP){
			lastClickTime = SystemClock.uptimeMillis();
			return true;
		}
		return false;
	}
	//note font size setting
	private static long lastClickTime2;
	public static boolean isValidClick(long millisecond){
		if(SystemClock.uptimeMillis() - lastClickTime2 > millisecond){
			lastClickTime2 = SystemClock.uptimeMillis();
			return true;
		}
		return false;
	}

	/**
	 * 验证码手机号
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("(13|14|15|17|18)\\d{9}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}


	public static boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager)Global.getContext().getSystemService(Context
				.CONNECTIVITY_SERVICE);
		if(connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/********************************begin***判断是否前台进程******************************************/
	public static boolean isRunningForeground(Context context){
		String packageName=getPackageName(context);
		String topActivityClassName=getTopActivityName(context);
		if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
			LogUtil.logi("---> isRunningForeGround");
			return true;
		} else {
			LogUtil.logi("---> isRunningBackGround");
			return false;
		}
	}
	public static String getTopActivityName(Context context){
		String topActivityClassName=null;
		ActivityManager activityManager =
				(ActivityManager)(context.getSystemService(Context.ACTIVITY_SERVICE )) ;
		List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
		if(runningTaskInfos != null && runningTaskInfos.size()>0){
			ComponentName f=runningTaskInfos.get(0).topActivity;
			topActivityClassName=f.getClassName();
		}
		return topActivityClassName;
	}

	public static String getPackageName(Context context){
		String packageName = context.getPackageName();
		return packageName;
	}

	/********************************end***判断是否前台进程******************************************/


	public static int getAppVersionCode(Context context){
		int versionCode = 1;
		String packageName = getAppPackageName(context);
		try {
			versionCode = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_CONFIGURATIONS).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getAppPackageName(Context context){
		return context.getPackageName();
	}

	public static boolean selfPermissionGranted(Context context, String permission) {
		// For Android < Android M, self permissions are always granted.
		boolean result = true;

		int targetSdkVersion = 0;
		try {
			final PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            result = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, context.getPackageName()));
        }

		return result;
	}

    /**
     * 判断摄像头是否可用
     * 主要针对6.0 之前的版本，现在主要是依靠try...catch... 报错信息，感觉不太好，
     * 以后有更好的方法的话可适当替换
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

	/**
	 * ScrollView嵌套listView 重新计算listView高度
	 * @param listView
     */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if(listView == null) return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
/*
	public static int (){
		int appVersion=0;
		PackageManager manager =Global.getContext().getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(Global.getContext().getPackageName(), 0);
			appVersion = info.versionCode;
			LogUtil.logi("GlobalUtils----------------->"+appVersion);
		} catch (PackageManager.NameNotFoundException e) {
			LogUtil.logi("---getAPPVersion: NameNotFoundException error---");
		}
		return appVersion;
	}*/

	//版本名
	public static String getAPPVersionName(Context context) {
		return  getPackageInfo(context)==null?"":getPackageInfo(context).versionName;
	}

	//版本号
	public static int getAPPVersionCode(Context context) {
		return getPackageInfo(context)==null?0:getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi;
		} catch (Exception e) {}
		return pi;
	}

	/** 检测GPS定位是否开启*/
	public static boolean isOpenGps(Context context){

	/*	String str = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) {
			return str.contains("gps");
		}
		else{
			return false;
		}
*/
		LocationManager locationManager = (LocationManager)context.getSystemService(Context
				.LOCATION_SERVICE);

		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	/**检测网络定位是否开启*/
	public static boolean isOpenNetworkProvider(Context context){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context
				.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 *
	 * @param fontScale
     */
	public static void setFontScale(float fontScale){
		Resources res = getResources();
		Configuration configuration = res.getConfiguration();
		configuration.fontScale = fontScale;

		/*DisplayMetrics displayMetrics = res.getDisplayMetrics();
		displayMetrics.density = 1 ;*/

		res.updateConfiguration(configuration,res.getDisplayMetrics());
	}


	public static Dialog getLoadingDialog(final Activity activity, String tvLoadingStr, boolean
			isCanceledOnTouchOutside){
		//  Dialog dlg = new Dialog(context, R.style.custom_dialog);
		Dialog dlg = new Dialog(activity){
			@Override
			public void show() {
				if(!activity.isFinishing())//防止activity销毁后出现的IllegalArgumentException
				   super.show();
			}

			@Override
			public void dismiss() {
				if(!activity.isFinishing())//防止activity销毁后出现的IllegalArgumentException
				   super.dismiss();
			}
		};
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View viewContent = inflate(activity, R.layout.fragment_layout_loading_progress_dialog, null);
		ProgressBar pb = (ProgressBar) viewContent.findViewById(R.id.iv_loading_progress_dialog_loadprogress);
		pb.setInterpolator(new AccelerateDecelerateInterpolator());
		pb.animate().setDuration(100);

		if(tvLoadingStr!=null && !TextUtils.isEmpty(tvLoadingStr)){
			TextView tvLoading = (TextView) viewContent.findViewById(R.id.more_data_msg);
			tvLoading.setText(""+tvLoadingStr);
		}

		dlg.setContentView(viewContent);
		//dlg.setCanceledOnTouchOutside(false);
		dlg.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
		dlg.setCancelable(true);
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		Window window = dlg.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.3f;
		lp.width = dm.widthPixels * 20 / 20;
		window.setAttributes(lp);
		//dlg.show();
		return dlg;
	}


	/** 全局单例保存变量   begin*/
	private static String phoneNum;
	public static void savePhoneNum(String phoneNum1){
		phoneNum = phoneNum1;
	}
	public static String getPhoneNum(){
		return phoneNum;
	}
	
	private static HashMap<String,String> thirdPartyLogin;
	public static void saveThirdPartyLogin(HashMap<String,String> map){
		thirdPartyLogin = map;
	}
	public static HashMap<String,String> getThirdPartyLogin(){
		return thirdPartyLogin;
	}
	/** 全局单例保存变量   end*/

	/** 根据id获取color*/
	public static int getColor(int colId){
		return getResources().getColor(colId);
	}

	public static void setWebView(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.sdk.WebViewClient client, com.tencent.smtt.sdk.WebChromeClient chromeClient){
		//关闭硬件加速，解决Webview黑屏或白屏或ANR问题
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.setWebViewClient(client);
		webView.setWebChromeClient(chromeClient);

		com.tencent.smtt.sdk.WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);

		settings.setSupportZoom(true); // 支持缩放
		//settings.setDomStorageEnabled(true);//开启DOM storage API功能，解决“重定向”无法自动跳转的问题
//		settings.setBlockNetworkImage(true);//阻止图片网络数据

		String ua = settings.getUserAgentString();
		settings.setUserAgentString(ua+"; ShuiYue /"+GlobalUtils
				.getAPPVersionName(Global.getContext()));

		//针对Android5.0以上webview进行设置，否则图片有可能显示不出来
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

		if(isOpenNetwork()){
			webView.clearCache(true);
	    }
		if(isOpenNetwork()){
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		}else {
			settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {	//调试的
				webView.setWebContentsDebuggingEnabled(true);
		}

	//	webView.addJavascriptInterface(new JsCallTokenInvalid(Global.getContext()), "tokenInvalid");
	}

	public static boolean closeInputStream(InputStream input){
		boolean isClose = false;
		if(input !=null){
			try {
				input.close();
				isClose = true;
			} catch (IOException e) {
				isClose = false;
			}
		}
		return isClose;
	}

	public static boolean closeOutputStream(OutputStream out) {
		boolean isClose = false;
		if (out != null) {
			try {
				out.close();
				isClose = true;
			} catch (IOException e) {
				isClose = false;
			}
		}
		return isClose;
	}



}
