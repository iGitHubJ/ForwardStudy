package com.zs.project.view.loadingview.updown;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zs.project.R;
import com.zs.project.util.LogUtil;

/**
 * Created by zzz40500 on 15/4/6.
 */
public class LoadingImageView extends FrameLayout {

    /**
     * 动画时间
     */
    private static final int ANIMATION_DURATION = 300;

    /**
     * 上升、降落时间
     */
    private static  float mDistance = 150;

    /**
     * 加速、减速
     */
    public float factor = 1.4f;

    private int flag;

    /**
     * 圆形  正方形  三角形
     */
    private ImageView mShapeLoadingView;

    /**
     * 阴影部分
     */
    private ImageView mIndicationIm;

    /**
     * 文字信息
     */
    private TextView mLoadTextView;

    private int mTextAppearance;

    private String mLoadText;

    private AnimatorSet mUpAnimSet;
    private AnimatorSet mDownAnimSet;

    private MyRunnable mRunnable;

    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.DownLoadingView);
        mLoadText = typedArray.getString(R.styleable.DownLoadingView_loadingText);
        mTextAppearance = typedArray.getResourceId(R.styleable.DownLoadingView_loadingTextAppearance, -1);
        typedArray.recycle();
    }


    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.load_image_view, null);

        mDistance = dip2px(54f);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        mShapeLoadingView = (ImageView) view.findViewById(R.id.loading_view);
        mIndicationIm = (ImageView) view.findViewById(R.id.indication);
        mLoadTextView = (TextView) view.findViewById(R.id.promptTV);

        if (mTextAppearance != -1) {
            mLoadTextView.setTextAppearance(getContext(), mTextAppearance);
        }
        setLoadingText(mLoadText);

        addView(view, layoutParams);

        postDelayed( mRunnable = new MyRunnable(), 200);

//        freeFall();
    }

    class MyRunnable implements Runnable{
        @Override
        public void run() {
            freeFall();
        }
    }



    public void setLoadingText(CharSequence loadingText) {

        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView.setVisibility(GONE);
        } else {
            mLoadTextView.setVisibility(VISIBLE);
        }
        mLoadTextView.setText(loadingText);
    }

    /**
     * 上抛
     */
    public void upThrow() {
        if (getContext() == null){
            return;
        }
        // 阴影伸缩 动画
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1, 0.2f);
        // 三个图标图标下降 动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", mDistance, 0);
        // 旋转动画
        ObjectAnimator objectAnimator1 = null;
        /*switch (mShapeLoadingView.getShape()) {
            case SHAPE_RECT:      // 正方形
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);
                break;
            case SHAPE_CIRCLE:    // 圆
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
            case SHAPE_TRIANGLE:  // 三角
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
        }*/

        switch (flag) {
            case 0:    // 三角
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);
                break;
            case 1:    // 圆
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
            case 2:    // 正方形
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
        }

        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator1.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator(factor));
        objectAnimator1.setInterpolator(new DecelerateInterpolator(factor));
        mUpAnimSet = new AnimatorSet();
        mUpAnimSet.setDuration(ANIMATION_DURATION);
        mUpAnimSet.playTogether(objectAnimator, objectAnimator1, scaleIndication);
        mUpAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mUpAnimSet.start();

    }

    /**
     * 下落
     */
    public void freeFall() {
        if (getContext() == null){
            return;
        }
        // 阴影伸缩 动画
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1);
        // 三个图标图标下降 动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", 0, mDistance);
        // 设置动画时间
        objectAnimator.setDuration(ANIMATION_DURATION);
        // 设置加速下落
        objectAnimator.setInterpolator(new AccelerateInterpolator(factor));

        mDownAnimSet = new AnimatorSet();
        mDownAnimSet.setDuration(ANIMATION_DURATION);
        // 两个动画同时进行
        mDownAnimSet.playTogether(objectAnimator, scaleIndication);
        mDownAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                mShapeLoadingView.changeShape();
                if (flag == 0){
                    mShapeLoadingView.setImageDrawable(getResources().getDrawable(R.drawable.rect));
                    flag = 1;
                }else if (flag == 1){
                    mShapeLoadingView.setImageDrawable(getResources().getDrawable(R.drawable.circle));
                    flag = 2;
                } else{
                    mShapeLoadingView.setImageDrawable(getResources().getDrawable(R.drawable.triangle));
                    flag = 0;
                }
                upThrow();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDownAnimSet.start();

    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mUpAnimSet != null){
            mUpAnimSet.removeAllListeners();
            mUpAnimSet.cancel();
            mUpAnimSet = null;
            LogUtil.Companion.logShowError("mUpAnimSet ========");
        }
        if (mDownAnimSet != null){
            mDownAnimSet.removeAllListeners();
            mDownAnimSet.cancel();
            mDownAnimSet = null;
            LogUtil.Companion.logShowError("mDownAnimSet ========");
        }
        removeCallbacks(mRunnable);
        super.onDetachedFromWindow();
    }

}
