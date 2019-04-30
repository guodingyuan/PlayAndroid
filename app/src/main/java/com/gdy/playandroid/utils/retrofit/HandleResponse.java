package com.gdy.playandroid.utils.retrofit;

/**
 * 用于处理返回的结果
 */
public abstract class HandleResponse<T>{
    public abstract void onHandleSuccess(T data);
    public void onHandleError(int errorCode){
    }
}