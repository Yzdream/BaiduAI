package com.yz.baiduai.ui;

import android.view.View;

import com.yanzhenjie.album.Album;
import com.yz.baiduai.R;
import com.yz.baiduai.common.mvvm.FmBaseFragment;
import com.yz.baiduai.databinding.FragmentHomeBinding;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.data.Constants;
import com.yz.utils.EventBusUtils;
import com.yz.utils.ImageBlur;

public class HomeFragment extends FmBaseFragment<FragmentHomeBinding, MainViewModel> {

    public static final int HOME_PAGE = 0;

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
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);

        //模糊背景
        ImageBlur.makeBlur(mBinding.tvGeneral, 8, getNonNullActivity());
        ImageBlur.makeBlur(mBinding.tvPlant, 10, getNonNullActivity());
        ImageBlur.makeBlur(mBinding.tvAnimal, 10, getNonNullActivity());
        ImageBlur.makeBlur(mBinding.tvIngredient, 15, getNonNullActivity());

        //观察识别结果
        mViewModel.getIntentDetails().observe(this, aBoolean -> {
            EventBusUtils.postSticky(mViewModel.getDetails().getValue());
            intentActivity(ImageDetailsActivity.class);
        });

        mBinding.tvGeneral.setOnClickListener(view -> chooseImage(Constants.GENERAL));
        mBinding.tvPlant.setOnClickListener(view -> chooseImage(Constants.PLANT));
        mBinding.tvAnimal.setOnClickListener(view -> chooseImage(Constants.AMIMAL));
        mBinding.tvIngredient.setOnClickListener(view -> chooseImage(Constants.INGREDIENT));
    }

    private void chooseImage(String type) {
        Album.image(getNonNullActivity())
                .multipleChoice()
                .camera(true)
                .columnCount(3)
                .selectCount(1)
                .onResult(result -> mViewModel.identify(type, result.get(0).getPath()))
                .start();
    }
}
