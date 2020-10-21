package com.yz.baiduai.common.mvvm;

import android.databinding.ViewDataBinding;


/**
 * 残梦
 * Created by dell on 2018/3/8.
 */

public abstract class FmBaseFragment<mBinding extends ViewDataBinding, mViewModel extends BaseViewModel> extends BaseFragment<mBinding, mViewModel> {

    //fragment
    // 首次初始化
    protected boolean mFirstInit = true;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mFirstInit) {
            return;
        }
        if (!hidden) {
//            MobclickAgent.onPageStart(getClass().getSimpleName());
        } else {
//            MobclickAgent.onPageEnd(getClass().getSimpleName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstInit) {
            mFirstInit = false;
//            MobclickAgent.onPageStart(getClass().getSimpleName());
        }
    }
}
