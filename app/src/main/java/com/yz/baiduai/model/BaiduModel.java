package com.yz.baiduai.model;

import android.text.TextUtils;

import com.yz.baiduai.SampleApplicationLike;
import com.yz.baiduai.SampleApplicationLike;
import com.yz.baiduai.common.mvvm.BaseRepository;
import com.yz.data.Constants;
import com.yz.data.bean.BaiduKey;
import com.yz.data.bean.ResultData;
import com.yz.data.http.ApiExecption;
import com.yz.data.http.HttpMethods;
import com.yz.baiduai.common.http.ResultDataCallBack;
import com.yz.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;

public class BaiduModel extends BaseRepository {

    public void getAccessToken(ResultDataCallBack<BaiduKey> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credentials");
        map.put("client_id", "YPGLNzlfbqoK2YXi6ondDwEc");
        map.put("client_secret", "INBYCdvHdlNcIV4vI1ajsFf59rDDArR7");
        addSubscribe(HttpMethods.getInStance().getAccessToken(map).subscribe(new Observer<BaiduKey>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }

            @Override
            public void onNext(BaiduKey baiduKey) {
                if (!TextUtils.isEmpty(baiduKey.getAccess_token())) {
                    callBack.onSuccess(baiduKey);
                } else {
                    callBack.onError(new ApiExecption(baiduKey.getError(), baiduKey.getError_description()));
                }
            }
        }));
    }

    public void identifyGeneral(Map<String, Object> map, ResultDataCallBack<ResultData> callBack) {
        String accessToken = String.valueOf(SharedPreferencesUtils.getParam(SampleApplicationLike.getContext(), Constants.ACCESS_TOKEN, ""));
        addSubscribe(HttpMethods.getInStance().getGeneral(map, accessToken).subscribe(new Observer<ResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }

            @Override
            public void onNext(ResultData resultData) {
                if (TextUtils.isEmpty(resultData.getError())) {
                    callBack.onSuccess(resultData);
                } else {
                    callBack.onError(new ApiExecption(resultData.getError(), resultData.getError_description()));
                }
            }
        }));
    }

    public void identifyPlant(Map<String, Object> map, ResultDataCallBack<ResultData> callBack) {
        String accessToken = String.valueOf(SharedPreferencesUtils.getParam(SampleApplicationLike.getContext(), Constants.ACCESS_TOKEN, ""));
        addSubscribe(HttpMethods.getInStance().getPlant(map, accessToken).subscribe(onObserver(callBack)));
    }

    public void identifyAnimal(Map<String, Object> map, ResultDataCallBack<ResultData> callBack) {
        String accessToken = String.valueOf(SharedPreferencesUtils.getParam(SampleApplicationLike.getContext(), Constants.ACCESS_TOKEN, ""));
        addSubscribe(HttpMethods.getInStance().getAnimal(map, accessToken).subscribe(onObserver(callBack)));
    }

    public void identifyIngredient(Map<String, Object> map, ResultDataCallBack<ResultData> callBack) {
        String accessToken = String.valueOf(SharedPreferencesUtils.getParam(SampleApplicationLike.getContext(), Constants.ACCESS_TOKEN, ""));
        addSubscribe(HttpMethods.getInStance().getIngredient(map, accessToken).subscribe(onObserver(callBack)));
    }

    public void identifyCarType(Map<String, Object> map, ResultDataCallBack<ResultData> callBack) {
        String accessToken = String.valueOf(SharedPreferencesUtils.getParam(SampleApplicationLike.getContext(), Constants.ACCESS_TOKEN, ""));
        addSubscribe(HttpMethods.getInStance().getCarType(map, accessToken).subscribe(onObserver(callBack)));
    }

    private Observer<ResultData> onObserver(ResultDataCallBack<ResultData> callBack) {
        return new Observer<ResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(e);
            }

            @Override
            public void onNext(ResultData resultData) {
                if (TextUtils.isEmpty(resultData.getError())) {
                    callBack.onSuccess(resultData);
                } else {
                    callBack.onError(new ApiExecption(resultData.getError(), resultData.getError_description()));
                }
            }
        };
    }
}
