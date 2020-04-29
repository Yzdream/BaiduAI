package com.yz.baiduai.common.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BindingAdapter<T, VDB extends ViewDataBinding> extends HolderRecyclerAdapter<T, BindingHolder<VDB>> {

    private @LayoutRes int mLayoutId;

    public BindingAdapter(Context context, List<T> listData, @LayoutRes int layoutId) {
        super(context, listData);
        this.mLayoutId = layoutId;
    }

    @Override
    public View buildConvertView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return inflate(mLayoutId, parent, false);
    }

    @Override
    public BindingHolder<VDB> buildHolder(View convertView, int viewType) {
        return new BindingHolder<>(convertView);
    }

    @Override
    public void bindViewDatas(BindingHolder<VDB> holder, T t, int position) {
        holder.mBinding.setVariable(com.yz.baiduai.BR.data, t);
        holder.mBinding.executePendingBindings();
    }

}

