package com.yz.baiduai.common.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.yz.baiduai.common.http.HttpErrorCallBack;
import com.yz.baiduai.common.http.HttpErrorInfo;

public abstract class BaseViewModel<M extends BaseRepository> extends AndroidViewModel {

    protected M mModel;

    //注册被观察者 监测是否需要展示加载
    private MutableLiveData<Boolean> isShowLoading = new MutableLiveData<>();
    //注册被观察者 监测需要展示的信息
    private MutableLiveData<String> message = new MutableLiveData<>();
    //注册被观察者 监测是否需要重新登录
    private MutableLiveData<Boolean> againLogin = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mModel = createModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mModel != null)
            mModel.unSubscribe();
    }

    protected abstract M createModel();

    protected MutableLiveData<Boolean> getIsShowLoading() {
        return isShowLoading;
    }

    protected MutableLiveData<String> getShowMessage() {
        return message;
    }

    protected MutableLiveData<Boolean> getAgainLogin() {
        return againLogin;
    }

    protected void onErrorMsg(Throwable e) {
        HttpErrorInfo.getErrorInfo(e, new HttpErrorCallBack() {
            @Override
            public void showError(String msg) {
                showMsg(msg);
            }

            @Override
            public void onLogin(String msg) {
                showMsg(msg);
                againLogin.setValue(true);
            }
        });
    }

    protected void showMsg(String msg) {
        message.setValue(msg);
    }

    protected void showLoading() {
        isShowLoading.setValue(true);
    }

    protected void hiddenLoading() {
        isShowLoading.setValue(false);
    }

}
