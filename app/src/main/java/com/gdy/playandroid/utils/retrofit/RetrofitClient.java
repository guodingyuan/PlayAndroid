package com.gdy.playandroid.utils.retrofit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gdy.playandroid.mvp.bean.ResponseBean;
import com.gdy.playandroid.utils.Global;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * RetrofitClient
 * Created by Tamic on 2016-06-15.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private static BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = BaseApiService.Base_URL;
    private static Context mContext=Global.getContext();
    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;


    public static RetrofitClient getInstance() {
        return new RetrofitClient();
    }

    public static RetrofitClient getInstance(String url) {
        return new RetrofitClient(url);
    }

    public static RetrofitClient getInstance(String url, Map<String, String> headers) {
        return new RetrofitClient(url, headers);
    }

   private RetrofitClient() {
       this(mContext, null, null);
   }
    private RetrofitClient(String url) {
        this(mContext, url, null);
    }


    private RetrofitClient(String url, Map<String, String> headers) {

        this(mContext, url, headers);
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if(headers==null){
            headers=new HashMap<>();
            //headers.put("authToken",SharedPreferencesUtil.getInstance().getString(SharedConstants.AUTHTOKEN));
        }

        if ( httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "gdy_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }


        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .allEnabledCipherSuites()
                .build();//解决在Android5.0版本以下https无法访问
        ConnectionSpec spec1 = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build();//兼容http接口
        RetrofitClient.okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor())
                //.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectionSpecs(Arrays.asList(spec,spec1))//解决在Android5.0版本以下https无法访问，并兼容http接口
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CaheInterceptor(context))
                .addNetworkInterceptor(new CaheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为15s
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .build();
        retrofit = new Retrofit.Builder()
                .client(RetrofitClient.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public static BaseApiService getApiService(){
        if(apiService==null){
            if(retrofit==null){
                getInstance();
            }
            apiService=retrofit.create(BaseApiService.class);
        }
        return apiService;
    }


    public Observable<ResponseBean> get(String url, Map<String, String> parameters) {
       return   apiService.executeGet(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformer());
    }

    public <T> Observable<ResponseBean> post(String url, Map<String, String> parameters) {
        return  apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformer());
    }

    /**
     * 普通，返回JsonObject
     */
    public Observable<JSONObject> postJ(String url, Map<String, String> parameters) {
         return apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerJsonObject());
    }

    public void json(String url, Map<String, String> map, BaseObserver baseObserver) {
        MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonMediaType, JSON.toJSONString(map));
        apiService.json(url, requestBody)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(baseObserver);
    }

    public void upload(String url, List<File> fileList, Map<String,String> map, BaseObserver baseObserver) {
        //多文件表单上传构造器
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        for(int i=0;i<fileList.size();i++){
            File file=fileList.get(i);
            if (file!=null && file.exists()) {
                multipartBodyBuilder.addFormDataPart("files"+i, getPictureName(file.getName()), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        apiService.json(url, multipartBodyBuilder.build())
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(baseObserver);
    }


    public void download(String url, CallBack callBack) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new DownSubscriber(callBack));
    }

    public ObservableTransformer<ResponseBody, ResponseBody> schedulersTransformer() {
        return new ObservableTransformer<ResponseBody, ResponseBody>() {
            @Override
            public ObservableSource<ResponseBody> apply(Observable<ResponseBody> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public ObservableTransformer<ResponseBody, ResponseBean> transformer() {
        return new ObservableTransformer<ResponseBody, ResponseBean>() {

            @Override
            public ObservableSource<ResponseBean> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new HandleFuc());
            }
        };
    }

    public ObservableTransformer<ResponseBody, JSONObject> transformerJsonObject() {

        return new ObservableTransformer<ResponseBody, JSONObject>() {

            @Override
            public ObservableSource<JSONObject> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new HandleJSONFuc());
            }
        };
    }

    public <T> Observable<T> switchSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private class HandleFuc implements Function<ResponseBody, ResponseBean> {

        @Override
        public ResponseBean apply(ResponseBody responseBody) throws Exception {
            return JSON.parseObject(responseBody.string(), ResponseBean.class);
        }
    }

    private class HandleJSONFuc implements Function<ResponseBody, JSONObject> {

        @Override
        public JSONObject apply(ResponseBody responseBody) throws Exception {
            return new JSONObject(responseBody.string());
        }
    }


    class DownSubscriber implements Observer<Response<ResponseBody>> {

        private CallBack callBack;

        public DownSubscriber(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Response<ResponseBody> response) {
            DownLoadManager.getInstance().writeResponseBodyToDisk(mContext, response,callBack);
        }

        @Override
        public void onError(Throwable t) {
            if (callBack != null) {
                callBack.onError(t);
            }
        }

        @Override
        public void onComplete() {
            if (callBack != null) {
                callBack.onCompleted();
            }
        }
    }

   private String getPictureName(String fileName){
       String ext;
       if (null == fileName || "".equals(fileName)) {
           ext="png";
       }else {
           ext = fileName.substring(fileName.lastIndexOf("."));
       }
       return System.currentTimeMillis()+"."+ext;
   }
}
