package com.yz.data.http;


import com.yz.data.respond.BaseRespond;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络线程转换
 * 过滤返回结果
 * Created by 残梦 on 2018/3/29.
 */

public class FilteredFactory {

    private final static Observable.Transformer transformer = new SimpleTransformer();
    private final static Observable.Transformer transformerList = new SimpleTransformerList();

    /**
     * 将Observable<BaseResponse<T>>转化Observable<T>,并处理BaseResponse
     *
     * @return 返回过滤后的Observable.
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable<T> compose(Observable<BaseRespond<T>> observable) {
        return observable.compose(transformer);
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<T> composeList(Observable<BaseRespond<T>> observable) {
        return observable.compose(transformerList);
    }

    /**
     * 转换Observable.
     */
    private static class SimpleTransformer<T> implements Observable.Transformer<BaseRespond<T>, T> {
        //这里对Observable,进行一般的通用设置.不用每次用Observable都去设置线程以及重连设置
        @Override
        public Observable<T> call(Observable<BaseRespond<T>> observable) {
            Observable<T> observable1 = null;
            try {
                observable1 = observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())/*.timeout(5, TimeUnit.SECONDS)*///重连间隔时间
                        .retry(3)//重连次数
                        .flatMap(new Func1<BaseRespond<T>, Observable<T>>() {
                            @Override
                            public Observable<T> call(BaseRespond<T> tBaseResponse) {
                                return flatResponse(tBaseResponse);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return observable1;
        }

        /**
         * 处理请求结果,BaseResponse
         *
         * @param response 请求结果
         * @return 过滤处理, 返回只有data数据的Observable
         */
        private Observable<T> flatResponse(final BaseRespond<T> response) throws RuntimeException {
            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    if (response.isStatus()) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(response.getDetail());
                        }
                    } else {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new ApiExecption(response.getErrorCode(), response.getUserMessage()));
                        }
                    }
                    if (!subscriber.isUnsubscribed()) {//请求完成
                        subscriber.onCompleted();
                    }
                }
            });
        }
    }

    /**
     * 转换Observable.
     */
    private static class SimpleTransformerList<T> implements Observable.Transformer<BaseRespond<T>, T> {
        //这里对Observable,进行一般的通用设置.不用每次用Observable都去设置线程以及重连设置
        @Override
        public Observable<T> call(Observable<BaseRespond<T>> observable) {
            Observable<T> observable1 = null;
            try {
                observable1 = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())/*.timeout(5, TimeUnit.SECONDS)*///重连间隔时间
                        .retry(3)//重连次数
                        .flatMap(new Func1<BaseRespond<T>, Observable<T>>() {
                            @Override
                            public Observable<T> call(BaseRespond<T> tBaseResponse) {
                                return flatResponseList(tBaseResponse);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return observable1;
        }

        /**
         * 处理请求结果,BaseResponse
         *
         * @param response 请求结果
         * @return 过滤处理, 返回只有data数据的Observable
         */
        private Observable<T> flatResponseList(final BaseRespond<T> response) throws RuntimeException {
            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    if (response.isStatus()) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(response.getList());
                        }
                    } else {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new ApiExecption(response.getErrorCode(), response.getUserMessage()));
                        }
                    }
                    if (!subscriber.isUnsubscribed()) {//请求完成
                        subscriber.onCompleted();
                    }
                }
            });
        }
    }
}

