package com.yz.baiduai.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.yz.baiduai.common.mvvm.BaseRepository;
import com.yz.baiduai.common.mvvm.BaseViewModel;


public class LoadingViewModel extends BaseViewModel {

    public LoadingViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected BaseRepository createModel() {
        return null;
    }
}
