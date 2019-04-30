package com.gdy.playandroid.utils.retrofit;

import android.text.TextUtils;


import com.gdy.playandroid.BuildConfig;
import com.gdy.playandroid.utils.LogUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {

    private static final boolean LOG_DEBUG = BuildConfig.DEBUG;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (LOG_DEBUG) {
            logForRequest(request);
        }
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            LogUtil.logd("method : " + request.method() + "  ║  url : " + url);
            if (headers != null && headers.size() > 0) {
                //Logger.d("headers : " + headers.toString());
            }

            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogUtil.logd("requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtil.logd("requestBody's content : " + bodyToString(request));
                    } else {
                        LogUtil.logd("requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private Response logForResponse(Response response) {
        if (LOG_DEBUG) {
            try {
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                LogUtil.logd("url : " + clone.request().url() + "  ║  code : " + clone.code() + "  ║  protocol : " + clone.protocol());
                if (!TextUtils.isEmpty(clone.message())){
                    //Logger.d("message : " + clone.message());
                    ResponseBody body = clone.body();
                    if (body != null) {
                        MediaType mediaType = body.contentType();
                        if (mediaType != null) {
                            //Logger.d("responseBody's contentType : " + mediaType.toString());
                            if (isText(mediaType)) {
                                String resp = body.string();
                                LogUtil.logd(resp);
                                //打印json格式或者xml格式日志
                               /* switch (mediaType.subtype()) {
                                    case "xml":
                                        Logger.xml(resp);
                                        break;
                                    case "json":
                                        Logger.json(resp);
                                        break;
                                    default:
                                        Logger.d(resp);
                                        break;
                                }*/
                                body = ResponseBody.create(mediaType, resp);
                                return response.newBuilder().body(body).build();
                            } else {
                                LogUtil.loge("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
