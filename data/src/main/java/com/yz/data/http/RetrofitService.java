package com.yz.data.http;


import com.yz.data.bean.BaiduKey;
import com.yz.data.bean.ResultData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * RetrofitService 网络请求接口
 * Created by dell on 2018/5/18.
 */

public interface RetrofitService {

    @GET("/oauth/2.0/token")
    Observable<BaiduKey> getAccessToken(@QueryMap Map<String, String> map);

    @POST("/rest/2.0/image-classify/v2/advanced_general")
    Observable<ResultData> getGeneral(@Body RequestBody body, @Query("access_token") String accessToken);

    @POST("/rest/2.0/image-classify/v1/plant")
    Observable<ResultData> getPlant(@Body RequestBody body, @Query("access_token") String accessToken);

    @POST("/rest/2.0/image-classify/v1/animal")
    Observable<ResultData> getAnimal(@Body RequestBody body, @Query("access_token") String accessToken);

    @POST("/rest/2.0/image-classify/v1/classify/ingredient")
    Observable<ResultData> getIngredient(@Body RequestBody body, @Query("access_token") String accessToken);

    @POST("/rest/2.0/image-classify/v1/car")
    Observable<ResultData> getCarType(@Body RequestBody body, @Query("access_token") String accessToken);

    @POST("/api/order/query")
    Observable<String> getOrderInfo(@Body RequestBody body);

}
