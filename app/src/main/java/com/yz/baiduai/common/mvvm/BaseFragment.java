package com.yz.baiduai.common.mvvm;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 残梦
 * Created by dell on 2018/3/8.
 */

public abstract class BaseFragment<mBinding extends ViewDataBinding, mViewModel extends BaseViewModel> extends Fragment {

    protected View mRoot;
    // 标示是否第一次初始化界面
    private boolean mIsFirstInitView = true;
    protected boolean isViewInitiated; //控件是否初始化完成

    protected mBinding mBinding;
    protected mViewModel mViewModel;

    private FragmentActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            // 得到界面Id并设置到Activity界面中
            mBinding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false);
            mBinding.setLifecycleOwner(this);
            //创建viewModel
            mViewModel = ViewModelProviders.of(getNonNullActivity()).get(getViewModelClass());
            //绑定viewModel
            dataBindingSetViewModel();
            initViewModel(mViewModel);
            if (mBinding == null) {
                int layId = getContentLayoutId();
                // 初始化当前的跟布局，但是不在创建时就添加到container里边
                mRoot = inflater.inflate(layId, container, false);
            } else {
                mRoot = mBinding.getRoot();
            }
            initView(mRoot);
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitView) {
            // 触发一次以后就不会触发
            mIsFirstInitView = false;
            // 触发
            onFirstInit();
        }
        isViewInitiated = true;
        // 当View创建完成后初始化数据
        initData();
    }

    protected abstract Class<mViewModel> getViewModelClass();

    protected abstract void dataBindingSetViewModel();

    @LayoutRes
    protected abstract int getContentLayoutId();

    protected void initView(View root) {

    }

    protected void onFirstInit() {

    }

    protected void initData() {

    }

    protected FragmentActivity getNonNullActivity() {
        return mActivity;
    }

    private void initViewModel(mViewModel viewModel) {
        ((BaseActivity) getNonNullActivity()).initViewModel(viewModel);
    }

    protected void intentActivity(Class newClass) {
        Intent intent = new Intent(getNonNullActivity(), newClass);
        startActivity(intent);
    }

}
