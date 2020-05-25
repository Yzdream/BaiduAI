package com.yz.baiduai.common.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.yz.baiduai.dialog.LoadingDialog;
import com.yz.utils.ToastHelper;
import com.yz.utils.app.Density;


/**
 * 残梦
 * Created by dell on 2018/6/1.
 */

public abstract class BaseActivity<mBinding extends ViewDataBinding, mViewModel extends BaseViewModel> extends AppCompatActivity {

    public mBinding mBinding;
    public mViewModel mViewModel;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();
        //是否使用适配方案
        if (initDensity()) Density.setDefault(this);
        // 得到界面Id并设置到Activity界面中
        mBinding = DataBindingUtil.setContentView(this, getContentLayoutId());
        if (mBinding != null) {
            mBinding.setLifecycleOwner(this);
        } else {
            setContentView(getContentLayoutId());
        }
        mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
        initViewModel(mViewModel);
        setLoading(mViewModel);
        dataBindingSetViewModel();
        initView(savedInstanceState);
        initData();
    }

    public void initViewModel(mViewModel mViewModel) {
        if (mViewModel != null) {
            setShowMessage(mViewModel);
            setLogin(mViewModel);
        }
    }

    protected abstract Class<mViewModel> getViewModelClass();

    /**
     * 初始化窗口
     */
    protected void initWidows() {
    }


    /**
     * 初始化适配方案 默认为适配
     *
     * @return 是否使用今日头条适配
     */
    protected boolean initDensity() {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    protected abstract void dataBindingSetViewModel();

    /**
     * 初始化控件
     */
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (loadingDialog != null && loadingDialog.getDialog() != null && loadingDialog.getDialog().isShowing()) {
                    loadingDialog.dismiss();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void intentActivity(Class newClass) {
        Intent intent = new Intent(this, newClass);
        startActivity(intent);
    }

    private void setLogin(mViewModel mViewModel) {
        mViewModel.getAgainLogin().observe(this, (Observer<Boolean>) o -> {
            if (o != null && o) {

            }
        });
    }

    private void setLoading(mViewModel mViewModel) {
        //观察到是否展示加载
        mViewModel.getIsShowLoading().observe(this, (Observer<Boolean>) aBoolean -> {
            if (aBoolean != null && aBoolean)
                showLoading();
            else
                hideLoading();
        });
    }

    public void setShowMessage(mViewModel mViewModel) {
        //观察msg是否改变
        mViewModel.getShowMessage().observe(this, (Observer<String>) msg -> {
            if (!TextUtils.isEmpty(msg)) {
                ToastHelper.showLongToast(msg);
            }
        });
    }

    public void showLoading() {
        hideLoading();
        loadingDialog = new LoadingDialog();
        loadingDialog.show(getSupportFragmentManager(), "loading");
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.getDialog() != null && loadingDialog.getDialog().isShowing()) {
            loadingDialog.dismiss();
        }
    }


}
