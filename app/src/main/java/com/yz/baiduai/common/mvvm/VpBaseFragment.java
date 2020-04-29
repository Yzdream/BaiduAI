package com.yz.baiduai.common.mvvm;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 残梦
 * Created by dell on 2018/3/8.
 */

public abstract class VpBaseFragment<mBinding extends ViewDataBinding, mViewModel extends BaseViewModel> extends BaseFragment<mBinding,mViewModel> {

    protected boolean isVisibleToUser; //页面是否可见
    protected boolean isDataInitiated; //数据是否加载

    //用于统计
    private boolean isStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareFetchData(false);
    }

    //先于oncreatview执行的方法
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData(false);
        if (!isViewInitiated || !isStart){
            return;
        }
        if (!isVisibleToUser) {
//            MobclickAgent.onPageEnd(getClass().getSimpleName());
            isStart = false;
        }
    }

    //重写此方法为true可每次强制刷新数据
    protected void prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            loadData();
            isDataInitiated = true;
        }
    }

    //viewpage懒加载数据
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisibleToUser) {
            isStart = true;
//            MobclickAgent.onPageStart(getClass().getSimpleName());
        }
    }
}
