package com.yz.baiduai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yz.utils.CustomClickListener;


public class StateLayout extends FrameLayout {

    private TextView tvTip;
    private ImageView imageView;
    private ProgressBar pbLoad;
    private View stateView;

    private Context mContext;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();//初始化View
    }

    private void initView() {
    /*    stateView = View.inflate(getContext(), R.layout.view_state, null);
        imageView = stateView.findViewById(R.id.iv_tip);
        tvTip = stateView.findViewById(R.id.tv_tip);
        pbLoad = stateView.findViewById(R.id.pb_load);*/
        addView(stateView);
        this.setVisibility(GONE);
        this.setClickable(true);
        this.setFocusable(true);
    }

    public StateLayout with(Context context) {
        mContext = context;
        return this;
    }

    public StateLayout setBackGroundColor(int color) {
        this.setBackgroundColor(color);
        return this;
    }

    public StateLayout hide() {
        tvTip.setVisibility(GONE);
        imageView.setVisibility(GONE);
        pbLoad.setVisibility(GONE);
        this.setVisibility(GONE);
        return this;
    }

    public StateLayout showLoading() {
        this.setVisibility(VISIBLE);
        tvTip.setVisibility(GONE);
        pbLoad.setVisibility(VISIBLE);
        imageView.setVisibility(GONE);
        return this;
    }

    public StateLayout setTip(String tip) {
        tvTip.setText(tip);
        tvTip.setVisibility(VISIBLE);
        return this;
    }

    public StateLayout setImage(int image, boolean isGif) {
       /* if (mContext!=null) {
            if (isGif) {
                Glide.with(mContext).asGif().load(image).into(imageView);
            } else {
                Glide.with(mContext).load(image).into(imageView);
            }
            imageView.setVisibility(VISIBLE);
        }*/
        return this;
    }

    public StateLayout setOnClick(CustomClickListener clickListener) {
        stateView.setOnClickListener(clickListener);
        return this;
    }

}
