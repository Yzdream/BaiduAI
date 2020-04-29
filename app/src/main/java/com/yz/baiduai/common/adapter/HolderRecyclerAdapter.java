package com.yz.baiduai.common.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class HolderRecyclerAdapter<T,H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H>{

    private Context context;

    private List<T> listData;

    private LayoutInflater layoutInflater;

    private OnItemToucherListener onItemToucherListener;

    public void setOnItemToucherListener(OnItemToucherListener onItemToucherListener){
        this.onItemToucherListener = onItemToucherListener;
    }

    public HolderRecyclerAdapter(Context context, List<T> listData){
        super();
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getLayoutInflater(){
        return layoutInflater;
    }

    public Context getContext(){
        return context;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = buildConvertView(layoutInflater,parent,viewType);
        return buildHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(H holder, final int position) {
        T t = position<listData.size() ? listData.get(position) : null;
        bindViewDatas(holder,t,position);
        holder.itemView.setOnClickListener(v -> {
            if (onItemToucherListener!=null)
                onItemToucherListener.onItemClick(v,position);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemToucherListener!=null)
                onItemToucherListener.onItemLongClick(v,position);
            return true;
        });
    }

    public void setOnClick(H holder, @IdRes int layoutId,int position){
        holder.itemView.findViewById(layoutId).setOnClickListener(v -> {
            if (onItemToucherListener!=null)
                onItemToucherListener.onItemChildClick(v,position);
        });
    }

    @Override
    public int getItemCount() {
        return listData==null ? 0:listData.size();
    }

    public T getItem(int position) {
        if (position < getItemCount()) {
            return getListData().get(position);
        }

        return null;
    }

    public void refreshData(List<T> list) {
        if (list != null) {
            setListData(list);
        } else {
            getListData().clear();
        }
        notifyDataSetChanged();

    }

    public View inflate(@LayoutRes int layoutId, ViewGroup parent, boolean attachToRoot){
        return layoutInflater.inflate(layoutId,parent,attachToRoot);
    }

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    /**
     * 建立convertView
     * @param layoutInflater
     * @param parent
     * @param viewType
     * @return
     */
    public abstract View buildConvertView(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    /**
     * 建立视图Holder
     * @param convertView
     * @param viewType
     * @return
     */
    public abstract H buildHolder(View convertView,int viewType);

    /**
     * 绑定数据
     * @param holder
     * @param t
     * @param position
     */
    public abstract void bindViewDatas(H holder,T t,int position);

}