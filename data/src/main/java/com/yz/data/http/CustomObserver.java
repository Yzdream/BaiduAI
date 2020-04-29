package com.yz.data.http;

import rx.Observer;

/**
 * 自定义订阅者
 * 拦截error
 * Created by 残梦 on 2018/3/29.
 */

public abstract class CustomObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onErrorMsg(e);
    }

    @Override
    public void onNext(T response) {
        onSuccess(response);
    }

    protected abstract void onSuccess(T response);

    protected abstract void onErrorMsg(Throwable msg);

}
