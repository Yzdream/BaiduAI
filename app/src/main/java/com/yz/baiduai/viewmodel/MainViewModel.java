package com.yz.baiduai.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yz.baiduai.MyApplication;
import com.yz.baiduai.model.BaiduModel;
import com.yz.baiduai.common.mvvm.BaseViewModel;
import com.yz.data.Constants;
import com.yz.data.bean.BaiduKey;
import com.yz.data.bean.Result;
import com.yz.data.bean.ResultData;
import com.yz.baiduai.common.http.ResultDataListener;
import com.yz.utils.Base64Util;
import com.yz.utils.FileUtil;
import com.yz.utils.SharedPreferencesUtils;
import com.yz.utils.exception.UncaughtException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel<BaiduModel> {

    private MutableLiveData<List<Result>> details = new MutableLiveData<>();

    private MutableLiveData<Boolean> intentDetails = new MutableLiveData<>();
    private Subscription subscription;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected BaiduModel createModel() {
        return new BaiduModel();
    }

    public void checkAccessToken() {
        if (TextUtils.isEmpty(String.valueOf(SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.ACCESS_TOKEN, "")))) {
            getAccessToken();
        } else {
            //有效时间  时间戳
            long time = (long) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.ACCESS_TOKEN_TIME, 0L);
            if (time - System.currentTimeMillis() <= 5 * 60 * 1000) {
                //失效时间提前5分钟 刷新
                getAccessToken();
            }
        }
    }

    private void getAccessToken() {
        mModel.getAccessToken(new ResultDataListener<BaiduKey>() {
            @Override
            public void onSuccess(BaiduKey baiduKey) {
                super.onSuccess(baiduKey);
                SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.ACCESS_TOKEN, baiduKey.getAccess_token());
                SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.ACCESS_TOKEN_TIME, System.currentTimeMillis() + (baiduKey.getExpires_in() * 1000));
                showMsg("鉴权成功！");
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                onErrorMsg(throwable);
            }
        });
    }

    public void identify(String type, String filePath) {
        showLoading();
        //子线程处理文件数据  主线程处理逻辑
        subscription = Observable.create((Observable.OnSubscribe<Map<String, Object>>) subscriber -> {
            try {
                //根据文件路径读取byte[] 数组
                byte[] imgData = FileUtil.readFileByBytes(filePath);
                //base64编码
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                Map<String, Object> map = new HashMap<>();
                map.put("image", imgParam);
                map.put("baike_num", 1);
                map.put("top_num", 1);
                subscriber.onNext(map);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UncaughtException.getInstance(getApplication()).exceptionThrowable(e);
                        showMsg("程序异常！");
                        hiddenLoading();
                    }

                    @Override
                    public void onNext(Map<String, Object> map) {
                        switch (type) {
                            case Constants.GENERAL:
                                mModel.identifyGeneral(map, resultDataListener);
                                break;
                            case Constants.PLANT:
                                mModel.identifyPlant(map, resultDataListener);
                                break;
                            case Constants.AMIMAL:
                                mModel.identifyAnimal(map, resultDataListener);
                                break;
                            case Constants.INGREDIENT:
                                mModel.identifyIngredient(map, resultDataListener);
                                break;
                            default:
                                mModel.identifyGeneral(map, resultDataListener);
                                break;
                        }
                    }
                });
    }

    private ResultDataListener<ResultData> resultDataListener = new ResultDataListener<ResultData>() {
        @Override
        public void onSuccess(ResultData resultData) {
            super.onSuccess(resultData);
            if (resultData.getResult() != null && resultData.getResult().size() > 0) {
                details.setValue(new ArrayList<>(Collections.singleton(resultData.getResult().get(0))));
                intentDetails.setValue(true);
            } else {
                showMsg("暂无结果！");
            }
            hiddenLoading();
        }

        @Override
        public void onError(Throwable throwable) {
            super.onError(throwable);
            onErrorMsg(throwable);
            hiddenLoading();
        }
    };

    @Override
    protected void onCleared() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        super.onCleared();
    }

    /* private void getPermissions() {
             if (!AndPermission.hasPermissions(MyApplication.getContext(), Permission.Group.CALENDAR)) {
                 AndPermission.with(MyApplication.getContext())
                         .runtime().permission(Permission.Group.CALENDAR)
                         .onGranted(data -> {

                         })
                         .onDenied(data -> showMsg("请到设置中开启权限，否则程序无法正常使用！"))
                         .start();
             }
         }
     */
    public MutableLiveData<List<Result>> getDetails() {
        return details;
    }

    public MutableLiveData<Boolean> getIntentDetails() {
        return intentDetails;
    }
}
