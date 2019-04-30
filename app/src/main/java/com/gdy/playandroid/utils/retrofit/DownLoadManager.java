package com.gdy.playandroid.utils.retrofit;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import com.gdy.playandroid.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Tamic on 2016-07-11.
 */
public class DownLoadManager {

    private static String fileSaveDir; //存放存储设备方式
    private static final String yhsoftPath = "/yhsoft/gttax/";
    private static String fileSavePath;

    private static final String TAG = "DownLoadManager";

    private static final String APK_CONTENTTYPE = "application/vnd.android.package-archive";
    private static final String IMG_CONTENTTYPE = "image/";

    private static Handler handler;

    private static DownLoadManager sInstance;

    /**
     *DownLoadManager getInstance
     */
    public static synchronized DownLoadManager getInstance() {
        if (sInstance == null) {
            sInstance = new DownLoadManager();
            handler = new Handler(Looper.getMainLooper());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                fileSaveDir=Environment.getExternalStorageDirectory().getPath();
            }else{
                fileSaveDir=Environment.getDataDirectory().getPath();
            }
            fileSavePath=fileSaveDir+yhsoftPath;
            File fileSave=new File(fileSavePath);
            if(!fileSave.exists()){
                fileSave.mkdirs();
            }
        }
        return sInstance;
    }



    public boolean  writeResponseBodyToDisk(Context context, Response<ResponseBody> response, final CallBack callBack) {

        String type =  response.headers().get("content-disposition");//通过请求头获取流的文件类型
        ResponseBody body = response.body();
        if(body!=null) {
            if (type == null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    String mediaTypeStr = mediaType.toString();
                    if (mediaTypeStr.startsWith(IMG_CONTENTTYPE)) {
                        type = mediaTypeStr.replace(IMG_CONTENTTYPE, "");
                    } else if (APK_CONTENTTYPE.equals(mediaTypeStr)){
                        type = "apk";
                    } else {
                        type = "txt";//默认值
                    }
                } else {
                    type = "txt";//默认值
                }
            }else {
                type=getFileType(type);
            }
            LogUtil.loge("哈哈哈："+type);
            final String name;
            if("apk".equals(type)){
                 name = "shuiyue."+ type;
            }else {
                 name = System.currentTimeMillis() +"."+ type;
            }

            final String path = fileSavePath + name;

            Log.d(TAG, "path:>>>>"+ path);
            try {
                // todo change the file location/name according to your needs
                File futureStudioIconFile = new File(path);

                if (futureStudioIconFile.exists()) {
                    futureStudioIconFile.delete();
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    final long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;
                    Log.d(TAG, "file length: "+ fileSize);
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                        Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                        if (callBack != null) {
                            final long finalFileSizeDownloaded = fileSizeDownloaded;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(fileSize!=0 && fileSize!=-1){
                                        int percent = (int) (finalFileSizeDownloaded*100/fileSize);
                                        callBack.onProgress(percent);
                                    }else {
                                        callBack.onProgress(-1);
                                    }
                                }
                            });

                        }
                    }

                    outputStream.flush();
                    Log.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                    if (callBack != null) {
                        handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSucess(path, name, fileSize);

                            }
                        });
                        Log.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    return true;
                } catch (IOException e) {
                    if (callBack != null) {
                        callBack.onError(e);
                    }
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                if (callBack != null) {
                    callBack.onError(e);
                }
                return false;
            }
        }else {
            return false;
        }
    }

    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            LogUtil.loge( "paramString---->null");
            return str;
        }
        LogUtil.loge( "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            LogUtil.loge( "i <= -1");
            return str;
        }
        str = paramString.substring(i + 1);
        LogUtil.loge("paramString.substring(i + 1)------>" + str);
        return str;
    }
}
