package com.yz.baiduai.common.mvvm;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yz.baiduai.R;
import com.yz.utils.ToastHelper;

import java.util.Objects;

public abstract class BaseDialogFragment<mBinding extends ViewDataBinding, mViewModel extends BaseViewModel> extends DialogFragment {

    private View rootView;

    protected float width = 0.75f;

    protected mBinding mBinding;
    protected mViewModel mViewModel;

    private FragmentActivity mActivity;

    private boolean isBottom = false;
    private float height = 0.0f;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            mBinding = DataBindingUtil.inflate(inflater, getRootViewId(), container, false);
            mBinding.setLifecycleOwner(this);
            //创建viewModel
            if (initToFragment()) {
                mViewModel = ViewModelProviders.of(mActivity).get(getViewModelClass());
            } else {
                mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
            }
            setShowMessage(mViewModel);
            //绑定viewModel
            dataBindingSetViewModel();
            rootView = mBinding.getRoot();
            setHeight(0);
            setIsBottom(false);
            initView();
        } else {
            if (rootView.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (isBottom) {
                width = 1;
                Window win = dialog.getWindow();
                // 一定要设置Background，如果不设置，window属性设置无效
                Objects.requireNonNull(win).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
                win.setWindowAnimations(R.style.dialog_anim);
                WindowManager.LayoutParams params = win.getAttributes();
                params.gravity = Gravity.BOTTOM;
               /* // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;*/
                win.setAttributes(params);
            }
            Objects.requireNonNull(dialog.getWindow()).setLayout((int) (dm.widthPixels * width), height == 0.0f ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (dm.heightPixels * height));
        }
    }

    public void setShowMessage(mViewModel mViewModel) {
        //观察msg是否改变
        mViewModel.getShowMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    ToastHelper.showLongToast(msg);
                }
            }
        });
    }

    protected boolean initToFragment() {
        return true;
    }

    protected void setIsBottom(boolean isBottom) {
        this.isBottom = isBottom;
    }

    protected void setHeight(float height) {
        this.height = height;
    }

    protected abstract int getRootViewId();

    protected abstract Class<mViewModel> getViewModelClass();

    protected abstract void dataBindingSetViewModel();

    protected abstract void initView();

    protected View getRootView() {
        return rootView;
    }
}
