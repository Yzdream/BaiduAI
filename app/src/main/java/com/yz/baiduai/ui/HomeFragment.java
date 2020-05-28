package com.yz.baiduai.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;

import com.yanzhenjie.album.Album;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.yz.baiduai.R;
import com.yz.baiduai.common.mvvm.FmBaseFragment;
import com.yz.baiduai.databinding.FragmentHomeBinding;
import com.yz.baiduai.manage.GlideImageLoader;
import com.yz.baiduai.viewmodel.MainViewModel;
import com.yz.data.Constants;
import com.yz.utils.EventBusUtils;
import com.yz.utils.ImageBlur;

import java.util.ArrayList;
import java.util.List;

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

        initText();
        initBanner();

        //观察识别结果
        mViewModel.getIntentDetails().observe(this, aBoolean -> {
            EventBusUtils.postSticky(mViewModel.getDetails().getValue());
            intentActivity(ImageDetailsActivity.class);
        });
    }

    private void initText() {
        Spannable spGeneral = new SpannableString(getString(R.string.general));
        spGeneral.setSpan(new AbsoluteSizeSpan(22, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spGeneral.setSpan(new AbsoluteSizeSpan(18, true), 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvGeneral.setText(spGeneral);
        Spannable spPlant = new SpannableString(getString(R.string.plant));
        spPlant.setSpan(new AbsoluteSizeSpan(22, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spPlant.setSpan(new AbsoluteSizeSpan(18, true), 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvPlant.setText(spPlant);
        Spannable spAnimal = new SpannableString(getString(R.string.animal));
        spAnimal.setSpan(new AbsoluteSizeSpan(22, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spAnimal.setSpan(new AbsoluteSizeSpan(18, true), 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvAnimal.setText(spAnimal);
        Spannable spIngredient = new SpannableString(getString(R.string.ingredient));
        spIngredient.setSpan(new AbsoluteSizeSpan(22, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spIngredient.setSpan(new AbsoluteSizeSpan(18, true), 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvIngredient.setText(spIngredient);
        Spannable spCarType = new SpannableString(getString(R.string.car_type));
        spCarType.setSpan(new AbsoluteSizeSpan(22, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spCarType.setSpan(new AbsoluteSizeSpan(18, true), 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvCarType.setText(spCarType);

        mBinding.cvGeneral.setOnClickListener(view -> chooseImage(Constants.GENERAL));
        mBinding.cvCarType.setOnClickListener(view -> chooseImage(Constants.CAR_TYPE));
        mBinding.cvPlant.setOnClickListener(view -> chooseImage(Constants.PLANT));
        mBinding.cvAnimal.setOnClickListener(view -> chooseImage(Constants.AMIMAL));
        mBinding.cvIngredient.setOnClickListener(view -> chooseImage(Constants.INGREDIENT));
    }

    private void initBanner() {
        List<String> images = new ArrayList<>();
        images.add(getStringFromDrawableRes(getNonNullActivity(), R.mipmap.bg_banner));
        images.add(getStringFromDrawableRes(getNonNullActivity(), R.mipmap.bg_banner1));
        //设置图片加载器
        mBinding.banner.setImageLoader(new GlideImageLoader());
        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置banner动画效果
        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置图片集合
        mBinding.banner.setImages(images);
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        mBinding.banner.start();
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

    public static String getStringFromDrawableRes(Context context, int id) {

        Resources resources = context.getResources();

        String path;
        path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"

                + resources.getResourcePackageName(id) + "/"

                + resources.getResourceTypeName(id) + "/"

                + resources.getResourceEntryName(id);

        return path;

    }
}
