package com.yz.baiduai.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.yz.baiduai.R;
import com.yz.baiduai.common.mvvm.BaseActivity;
import com.yz.baiduai.databinding.ActivityMainBinding;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.utils.BarUtils;
import com.yz.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private long backTime = 0;
    //Fragment集合
    private List<Fragment> FragmentList;
    //上一个展示的Fragment 用于控制Fragment显示
    private int lastIndex = 0;

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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
      /*  当界面销毁时不保存状态  使Fragment也重新创建
        super.onSaveInstanceState(outState, outPersistentState);*/
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //全屏
        windowTranslucentStatus();
        BarUtils.setStatusBar(MainActivity.this, false);

        initFragment();
        //注册系统时间每分钟改变的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(broadcastReceiver, filter);

        //检验token是否失效
        mViewModel.checkAccessToken();
    }

    private void initFragment() {
        FragmentList = new ArrayList<>();
        FragmentList.add(new HomeFragment());
        FragmentList.add(new MyFragment());
        selectFragment(HomeFragment.HOME_PAGE);
        //绑定底部导航栏
        mBinding.bnvView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.main_home:
                    selectFragment(HomeFragment.HOME_PAGE);
                    return true;
                case R.id.main_my:
                    selectFragment(MyFragment.MY_PAGE);
                    return true;
                default:
                    break;
            }
            return false;
        });
    }


    /**
     * Fragment碎片管理
     *
     * @param index 要显示的界面 从0开始
     */
    private void selectFragment(int index) {
        //获取Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = FragmentList.get(index);
        Fragment lastFragment = FragmentList.get(lastIndex);
        lastIndex = index;
        transaction.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            transaction.add(R.id.fl_content, currentFragment);
        }
        transaction.show(currentFragment);
        //提交事务
        transaction.commitAllowingStateLoss();
    }

    private void windowTranslucentStatus() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - backTime > 2000) {
                    ToastHelper.showShortToast("再按一次退出程序");
                    backTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
