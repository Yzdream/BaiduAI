package com.yz.baiduai.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yz.baiduai.R;
import com.yz.baiduai.common.mvvm.BaseActivity;
import com.yz.baiduai.databinding.ActivityPayTestBinding;
import com.yz.baiduai.viewmodel.AboutAppViewModel;
import com.yz.utils.CustomClickListener;

public class PayTestActivity extends BaseActivity<ActivityPayTestBinding, AboutAppViewModel> {

    @Override
    protected Class<AboutAppViewModel> getViewModelClass() {
        return AboutAppViewModel.class;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_pay_test;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mBinding.btnSelOrder.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick(View v) {
                mViewModel.getOrderInfo();
            }
        });
    }
}
