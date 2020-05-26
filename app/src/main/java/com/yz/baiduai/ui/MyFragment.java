package com.yz.baiduai.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tencent.bugly.beta.Beta;
import com.yz.baiduai.R;
import com.yz.baiduai.common.mvvm.FmBaseFragment;
import com.yz.baiduai.databinding.FragmentMyBinding;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.utils.ToastHelper;
import com.yz.utils.app.AppUtils;

public class MyFragment extends FmBaseFragment<FragmentMyBinding, MainViewModel> {

    public static final int MY_PAGE = 1;

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);

        mBinding.tvVersionChecked.setOnClickListener(onClickListener);
        mBinding.tvAppEvaluation.setOnClickListener(onClickListener);
        mBinding.tvAppExplain.setOnClickListener(onClickListener);
        mBinding.tvWrittenRecords.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.tv_version_checked:
                Beta.checkUpgrade(true, false);
                break;
            case R.id.tv_app_evaluation:
                evaluation();
                break;
            case R.id.tv_app_explain:
                intentActivity(AboutAppActivity.class);
                break;
            case R.id.tv_written_records:
                break;
        }
    };

    private void evaluation(){
        try{
            Uri uri = Uri.parse("market://details?id="+AppUtils.getPackageName(getNonNullActivity()));
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch(Exception e){
            ToastHelper.showShortToast("您的手机没有安装Android应用市场");
            e.printStackTrace();
        }
    }
}
