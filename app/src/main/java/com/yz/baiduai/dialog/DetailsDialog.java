package com.yz.baiduai.dialog;

import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yz.baiduai.R;
import com.yz.baiduai.databinding.ActivityDetailsBinding;
import com.yz.baiduai.databinding.ItemDataBinding;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.baiduai.common.adapter.BindingAdapter;
import com.yz.baiduai.common.mvvm.BaseDialogFragment;
import com.yz.data.bean.Result;

public class DetailsDialog extends BaseDialogFragment<ActivityDetailsBinding, MainViewModel> {

    @Override
    protected int getRootViewId() {
        return R.layout.activity_details;
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected void setIsBottom(boolean isBottom) {
        super.setIsBottom(true);
    }

    @Override
    protected void initView() {
        Logger.i(new Gson().toJson(mViewModel.getDetails().getValue()));
        if (getDialog() != null)
            getDialog().setCanceledOnTouchOutside(false);
        mBinding.rvDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        BindingAdapter<Result, ItemDataBinding> itemAdapter = new BindingAdapter<>(getActivity(), mViewModel.getDetails().getValue(), R.layout.item_data);
        mBinding.rvDetails.setAdapter(itemAdapter);
    }
}
