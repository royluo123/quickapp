package com.roy.quickapp.widget.anima;

import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * <b>AnimationListenerProxy简介:AnimationListener代理者，将动画监听统一返回到主线程处理</b>
 */
public class AnimationListenerProxy implements AnimationListener {

    private AnimationListener mListener;

    private Handler mListenerHanlder = new Handler(Looper.getMainLooper());

    public AnimationListenerProxy(AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart(final Animation animation) {
        if (mListener == null) {
            return;
        }
        mListenerHanlder.postAtFrontOfQueue(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onAnimationStart(animation);
                }
            }
        });
    }

    @Override
    public void onAnimationEnd(final Animation animation) {
        if (mListener == null) {
            return;
        }
        mListenerHanlder.postAtFrontOfQueue(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onAnimationEnd(animation);
                }
            }
        });
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        if (mListener == null) {
            return;
        }
        mListenerHanlder.postAtFrontOfQueue(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onAnimationRepeat(animation);
                }
            }
        });
    }

}
