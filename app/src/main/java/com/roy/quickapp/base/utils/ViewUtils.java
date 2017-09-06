package com.roy.quickapp.base.utils;

import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/8/30.
 */

public class ViewUtils {
    public static byte VIEW_STATE_SWITCH_IN = 0;
    public static byte VIEW_STATE_SWITCH_OUT = 1;

    public static void removeViewFromParent(View aView){
        if (aView == null) {
            return ;
        }
        ViewGroup sParentView = (ViewGroup) aView.getParent();
        if(sParentView != null){
            sParentView.removeView(aView);
        }
    }

    /**
     * <p>功能描述：设置View的alpha值，SDK版本低于11时没有setAlpha这个接口，所以通过透明度动画来实现透明度效果，SDK版本高于11时通过反射获取setAlpha接口</p>
     * @param view
     * @param alpha
     */
    public static void setViewAlpha(View view, float alpha) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            view.startAnimation(createAlphaAnimation(alpha, alpha));
        } else {
            view.setAlpha(alpha);
        }
    }

    /**
     * <p>功能描述：设置View是否可操作</p>
     * @param view
     * @param enabled
     */
    public static void setViewEnable(View view, boolean enabled) {
        if (view == null) {
            return ;
        }
        if (enabled) {
            setViewAlpha(view, 1.0f);
        } else {
            setViewAlpha(view, 0.3f);
        }
        view.setEnabled(enabled);
    }

    private static Animation createAlphaAnimation(float fromAlpha, float toAlpha) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    /**
     * <p>功能描述：判断是否当前View被点击</p>
     * @param view
     * @param ev
     * @return
     */
    public static boolean isViewHit(View view, ViewGroup parent, MotionEvent ev) {
        if (view == null || parent == null || ev == null) {
            return false;
        }
        Rect frame = new Rect();
        view.getHitRect(frame);
        final float xf = ev.getX();
        final float yf = ev.getY();
        final int scrolledXInt = (int) (xf + parent.getScrollX());
        final int scrolledYInt = (int) (yf + parent.getScrollY());
        if (frame.contains(scrolledXInt, scrolledYInt)) {
            return true;
        }
        return false;
    }

    /**
     * <p>功能描述：判断index是否在ListView的可见区域内</p>
     * @param lv
     * @param index
     * @return
     */
    public static boolean isInListViewVisibleZone(ListView lv, int index) {
        if (lv == null) {
            return false;
        }
        // 得到第1个可显示控件的位置,记住是第1个可显示控件噢。而不是第1个控件
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        // 最后一个可显示控件的位置
        int lastVisiblePosition = lv.getLastVisiblePosition();
        if (index < firstVisiblePosition || index > lastVisiblePosition) {
            // 如果不在显示界面，则直接返回
            return false;
        }
        return true;
    }

    /**
     * <p>功能描述：获取ListView第index个对象的子View，如果当前index不在显示范围内，则返回null</p>
     * @param lv
     * @param index
     * @return
     */
    public static View getListViewChildViewAt(ListView lv, int index) {
        if (lv == null || index < 0) {
            return null;
        }
        if (!isInListViewVisibleZone(lv, index)) {
            return null;
        }
        return lv.getChildAt(index - lv.getFirstVisiblePosition());
    }
}
