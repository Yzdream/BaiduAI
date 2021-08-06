package com.yz.data.http;


import com.yz.data.bean.BaiduKey;
import com.yz.data.bean.ResultData;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpMethods {

    //    public final static String BASE_URL = "https://aip.baidubce.com";
    public final static String BASE_URL = "https://aip.baidubce.com/";
    //
    private RequestBodyHelper bodyHelper = RequestBodyHelper.getInstance("application/json; charset=utf-8");
    //    private RequestBodyHelper bodyHelper = RequestBodyHelper.getInstance("application/x-www-form-urlencoded");
    private static final HttpMethods INSTANCE = new HttpMethods();

    public static HttpMethods getInStance() {
        return INSTANCE;
    }

    private <T> Observable<T> onThread(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())/*.timeout(5, TimeUnit.SECONDS)*///重连间隔时间
                .retry(0);//重连次数
    }

    public Observable<BaiduKey> getAccessToken(Map<String, String> map) {
        return onThread(RetofitHttp.getInstance().getAccessToken(map));
    }

    public Observable<ResultData> getGeneral(Map<String, Object> map, String accessToken) {
        return onThread(RetofitHttp.getInstance().getGeneral(bodyHelper.getRequestBody(map), accessToken));
    }

    public Observable<ResultData> getPlant(Map<String, Object> map, String accessToken) {
        return onThread(RetofitHttp.getInstance().getPlant(bodyHelper.getRequestBody(map), accessToken));
    }

    public Observable<ResultData> getAnimal(Map<String, Object> map, String accessToken) {
        return onThread(RetofitHttp.getInstance().getAnimal(bodyHelper.getRequestBody(map), accessToken));
    }

    public Observable<ResultData> getIngredient(Map<String, Object> map, String accessToken) {
        return onThread(RetofitHttp.getInstance().getIngredient(bodyHelper.getRequestBody(map), accessToken));
    }

    public Observable<ResultData> getCarType(Map<String, Object> map, String accessToken) {
        return onThread(RetofitHttp.getInstance().getCarType(bodyHelper.getRequestBody(map), accessToken));
    }

    public Observable<String> getOrderInfo(Map<String, String> map) {
        return onThread(RetofitHttp.getInstance().getOrderInfo(bodyHelper.createObjectBody(map)));
    }
}
