package com.yz.baiduai.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yz.baiduai.R;
import com.yz.baiduai.common.adapter.BindingAdapter;
import com.yz.baiduai.common.mvvm.BaseActivity;
import com.yz.baiduai.databinding.ActivityImageDetailsBinding;
import com.yz.baiduai.databinding.ItemDataBinding;
import com.yz.baiduai.viewmodel.ImageDetailsViewModel;
import com.yz.data.bean.Result;
import com.yz.utils.CustomClickListener;
import com.yz.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ImageDetailsActivity extends BaseActivity<ActivityImageDetailsBinding, ImageDetailsViewModel> {

    @Override
    protected Class<ImageDetailsViewModel> getViewModelClass() {
        return ImageDetailsViewModel.class;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_image_details;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        EventBusUtils.register(this);

        mBinding.ivBack.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick(View v) {
                finish();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getEventListData(List<Result> list){
        mBinding.rvDetails.setLayoutManager(new LinearLayoutManager(this));
        BindingAdapter<Result, ItemDataBinding> itemAdapter = new BindingAdapter<>(this, list, R.layout.item_data);
        mBinding.rvDetails.setAdapter(itemAdapter);
        EventBusUtils.removeStickyEvent(list.getClass());
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }
}
