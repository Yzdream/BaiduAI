package com.yz.baiduai.common.adapter;

import android.view.View;

public interface OnItemTouchListener {

    void onItemClick(View view, int position);

    void onItemChildClick(View view, int position);

    void onItemLongClick(View view, int position);
}
