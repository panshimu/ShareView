package com.miaozi.shareview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * created by panshimu
 * on 2019/8/22
 */
public class LoadingView extends LinearLayout {

    private ShareView mShareView;
    private View mViewMin;
    private int mTranslationDistance;
    //动画执行时间
    private final long mAnimationDuration = 1000;
    private AnimatorSet mFallAnimatorSet;
    private AnimatorSet mUpAnimatorSet;
    private boolean isStopAnimation = false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(100);
        setGravity(Gravity.CENTER);
        initLayout();
    }

    private int dip2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,i,getResources().getDisplayMetrics());
    }

    /**
     * 初始化加载布局
     */
    private void initLayout() {
        inflate(getContext(), R.layout.loading_ui, this);
        mShareView = findViewById(R.id.shapeView);
        mViewMin = findViewById(R.id.v_min);
        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimation();
            }
        });
    }

    /**
     * 下落动画
     */
    private void startFallAnimation() {

        if(mFallAnimatorSet == null) {
            ObjectAnimator translationAnimation = ObjectAnimator.ofFloat(mShareView, "translationY", 0, mTranslationDistance);
            //配合底部阴影缩小
            ObjectAnimator scaleAnimation = ObjectAnimator.ofFloat(mViewMin, "scaleX", 1f, 0.3f);

            //组合动画
            mFallAnimatorSet = new AnimatorSet();
            mFallAnimatorSet.playTogether(translationAnimation, scaleAnimation);
            mFallAnimatorSet.setDuration(mAnimationDuration);
            //设置差值器 越来越快
            mFallAnimatorSet.setInterpolator(new AccelerateInterpolator());

            // AnimatorListenerAdapter 只需要关注结束动画就行
            mFallAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //下落完弹起
                    if(!isStopAnimation) {
                        mShareView.exchange();
                        startUpAnimation();
                    }
                }
            });
        }
        mFallAnimatorSet.start();
    }

    /**
     * 上升动画
     */
    private void startUpAnimation(){

        Log.d("TAG","startUpAnimation");

        if(mUpAnimatorSet == null) {

            ObjectAnimator translationAnimation = ObjectAnimator.ofFloat(mShareView, "translationY", mTranslationDistance, 0);

            ObjectAnimator scaleAnimation = ObjectAnimator.ofFloat(mViewMin, "scaleX", 0.3f, 1f);

            ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mShareView, "rotation", 0, -360);

            //组合动画
            mUpAnimatorSet = new AnimatorSet();
            mUpAnimatorSet.playTogether(translationAnimation, scaleAnimation, rotateAnimation);
            mUpAnimatorSet.setDuration(mAnimationDuration);
            //设置差值器 越来越慢
            mUpAnimatorSet.setInterpolator(new DecelerateInterpolator());

            // AnimatorListenerAdapter 只需要关注结束动画就行
            mUpAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //弹起下落
                    if(!isStopAnimation) {
                        startFallAnimation();
                    }
                }
            });
        }
        mUpAnimatorSet.start();
    }

    public void showLoading(){
        setVisibility(VISIBLE);
        isStopAnimation = false;
        startFallAnimation();
    }
    public void hideLoading(){
        setVisibility(INVISIBLE);
        doCancel();
    }
    /**
     * 当设置隐藏的时候 需要关闭动画 内存优化
     * @param
     */
    public void doCancel() {
        mShareView.clearAnimation();
        mViewMin.clearAnimation();
        mUpAnimatorSet.cancel();
        mFallAnimatorSet.cancel();
        mUpAnimatorSet = null;
        mFallAnimatorSet = null;
        isStopAnimation = true;
    }
}
