package com.yz.baiduai.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.yanzhenjie.album.Album;
import com.yz.baiduai.R;
import com.yz.baiduai.databinding.ActivityMainBinding;
import com.yz.baiduai.dialog.DetailsDialog;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.baiduai.common.mvvm.BaseActivity;
import com.yz.data.Constants;
import com.yz.utils.BarUtils;
import com.yz.utils.ImageBlur;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                //系统每分钟会发出该广播
                mViewModel.checkAccessToken();
            }
        }
    };

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void dataBindingSetViewModel() {
        mBinding.setModel(mViewModel);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        windowTranslucentStatus();
        BarUtils.setStatusBar(MainActivity.this, false);

        //注册系统时间每分钟改变的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(broadcastReceiver, filter);

        //检验token是否失效
        mViewModel.checkAccessToken();

        //模糊背景
        ImageBlur.makeBlur(mBinding.tvGeneral, 8, this);
        ImageBlur.makeBlur(mBinding.tvPlant, 10, this);
        ImageBlur.makeBlur(mBinding.tvAnimal, 10, this);
        ImageBlur.makeBlur(mBinding.tvIngredient, 15, this);

        //观察识别结果
        mViewModel.getIntentDetails().observe(this, aBoolean -> new DetailsDialog().show(getSupportFragmentManager(), "data"));

        mBinding.tvGeneral.setOnClickListener(view -> chooseImage(Constants.GENERAL));
        mBinding.tvPlant.setOnClickListener(view -> chooseImage(Constants.PLANT));
        mBinding.tvAnimal.setOnClickListener(view -> chooseImage(Constants.AMIMAL));
        mBinding.tvIngredient.setOnClickListener(view -> chooseImage(Constants.INGREDIENT));
    }

    private void chooseImage(String type) {
        Album.image(MainActivity.this)
                .multipleChoice()
                .camera(true)
                .columnCount(3)
                .selectCount(1)
                .onResult(result -> mViewModel.identify(type, result.get(0).getPath()))
                .start();
    }

    private void windowTranslucentStatus() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
