package com.yz.baiduai.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.yz.baiduai.common.mvvm.BaseRepository;
import com.yz.baiduai.common.mvvm.BaseViewModel;
import com.yz.data.http.HttpMethods;
import com.yz.utils.Md5Util;
import com.yz.utils.StringMapUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;


public class AboutAppViewModel extends BaseViewModel {

    public AboutAppViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected BaseRepository createModel() {
        return null;
    }

    public void getOrderInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("mch_id", "123456");
        map.put("mch_order_no", "123456");
        map.put("sign", Md5Util.MD5(StringMapUtils.createLinkString(map)+"&key=0mmd47egkh9cwbdiasv0noerapmoywqn"));
        HttpMethods.getInStance().getOrderInfo(map).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {

            }
        });
    }
}
