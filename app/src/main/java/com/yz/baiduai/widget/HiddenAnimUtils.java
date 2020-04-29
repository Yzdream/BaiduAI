package com.yz.baiduai.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import com.yz.utils.app.DisplayUtils;

/**
 * Created by debbytang.
 * Description:显示隐藏布局的属性动画(铺展)
 * Date:2017/3/30.
 */
public class HiddenAnimUtils {

    private int mHeight;//伸展高度

    private View hideView, down;//需要展开隐藏的布局，开关控件

    private RotateAnimation animation;//旋转动画

    private boolean isOpen = false;

    private int rows = 1;

    private static Context context;
    private onAnimatorListen animatorListen;

    public HiddenAnimUtils(Context mContext) {
        context = mContext;
    }

    public void setData(View hideView, int height, int rows) {
        this.hideView = hideView;
        float mDensity = context.getResources().getDisplayMetrics().density;
        mHeight = (int) (mDensity * height + 0.5);//伸展高度
        this.rows = rows;
    }

    /**
     * 开关
     */
    public void toggle() {
//        startAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    public boolean isVisible() {
        return View.VISIBLE == hideView.getVisibility();
    }

//    /**
//     * 开关旋转动画
//     */
//    private void startAnimation() {
//        if (View.VISIBLE == hideView.getVisibility()) {
//            animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        } else {
//            animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        }
//        animation.setDuration(30);//设置动画持续时间
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
//        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        down.startAnimation(animation);
//    }

    private void openAnim(View v) {
        v.setVisibility(View.VISIBLE);
        isOpen = true;
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.start();
        if (animatorListen != null) animatorListen.onMonthShow();
    }

    public static void refreshView(View v, int mHeight) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = DisplayUtils.dp2px(context, mHeight);
        v.setLayoutParams(layoutParams);
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        isOpen = false;
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, final int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(450);//600
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                if (!isOpen && value < 50 && animatorListen != null) {
                    animatorListen.onWeekShow();
                    v.setVisibility(View.GONE);
                }
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void setOnAnimatorListen(onAnimatorListen animatorListen) {
        this.animatorListen = animatorListen;
    }

    public interface onAnimatorListen {
        void onWeekShow();

        void onMonthShow();
    }
}
