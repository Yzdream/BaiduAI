package com.yz.baiduai.dialog;

import com.yz.baiduai.R;
import com.yz.baiduai.databinding.DialogLoadingBinding;
import com.yz.baiduai.viewmodel.LoadingViewModel;
import com.yz.baiduai.common.mvvm.BaseDialogFragment;

public class LoadingDialog extends BaseDialogFragment<DialogLoadingBinding, LoadingViewModel> {

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected Class<LoadingViewModel> getViewModelClass() {
        return LoadingViewModel.class;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        mBinding.layoutLoading.show();
    }


    @Override
    public void dismiss() {
        mBinding.layoutLoading.hide();
        super.dismiss();
    }
}
