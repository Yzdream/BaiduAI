package com.yz.baiduai.common.http;

public interface ResultDataCallBack<T> {

    void onSuccess(T t);

    void onNoData();

    void onLastPage();

    void onError(Throwable throwable);

}
