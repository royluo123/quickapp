package com.roy.quickapp.framework.window;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.roy.quickapp.base.utils.ViewUtils;
import com.roy.quickapp.widget.anima.AnimationListenerProxy;

import java.util.Stack;


/**
 * Created by Administrator on 2017/8/30.
 */
public class WindowManager {
    private Context mContext;

    private FrameLayout mRootView;

    private Stack<View> mViews;


    public WindowManager(Context context){
        mContext = context;
        mViews = new Stack<View>();
    }

    private FrameLayout getRootView(){
        if(mRootView == null){
            mRootView = new FrameLayout(mContext);
            if(mContext instanceof Activity){
                ((Activity)mContext).setContentView(mRootView);
            }
        }

        return mRootView;
    }

    public View getContentView(){
        if(mViews.size() > 0){
            return mViews.peek();
        }

        return null;
    }

    public void setContentView(View view){
        setContentView(view, null);
    }

    public void setContentView(View view, Animation animation){
        setContentView(view, animation, null);
    }

    public void addCustomView(View view, FrameLayout.LayoutParams params){
        addCustomViewInRoot(view,params);
    }

    public void removeCustomView(View view){
        getRootView().removeView(view);
    }

    private void addCustomViewInRoot(View view,FrameLayout.LayoutParams params){
        if(view == null && mViews.size() < 1){
            return;
        }
        ViewUtils.removeViewFromParent(view);
        getRootView().addView(view, params);
    }


    public void setContentView(View view, Animation animation, ViewManagerListener listener){
        if(mViews.size() > 0 && mViews.peek() == view){
            return;
        }
        mViews.push(view);
        addViewInRoot(view, animation, false, listener);
    }

    private void addViewInRoot(View view, Animation animation, final boolean needRepalcePreviousView){
        addViewInRoot(view, animation, needRepalcePreviousView, null);
    }

    private void addViewInRoot(final View view, Animation animation, final boolean needRepalcePreviousView, final ViewManagerListener listener){
        if(view == null && mViews.size() < 1){
            return;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.LEFT | Gravity.TOP);

        ViewUtils.removeViewFromParent(view);
        getRootView().addView(view, params);

        if(animation != null){
            view.setAnimation(animation);
            AnimationListenerProxy proxy = new AnimationListenerProxy(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    checkViews(needRepalcePreviousView);
                    if (listener != null) {
                        listener.onViewAdded(view);
                    }
                }
            });
            animation.setAnimationListener(proxy);
            view.startAnimation(animation);
        }else{
            checkViews(needRepalcePreviousView);
        }
    }

    private void checkViews(boolean needRepalcePreviousView){
        int viewCount = getRootView().getChildCount();
        if(mViews.size() == 1){
            if(viewCount > 1){
                getRootView().removeViews(0, viewCount - 1);
            }
        }else{
            if(needRepalcePreviousView){
                replacePreView();
            }else{
                if (viewCount > 2) {
                    getRootView().removeViews(0, viewCount - 2);
                }
            }
        }
    }

    private void replacePreView(){
        getRootView().removeViews(0, getRootView().getChildCount() - 1);

        View preView = mViews.get(mViews.size() - 2);
        ViewUtils.removeViewFromParent(preView);
        getRootView().addView(preView, 0);
    }


    public void resetContentView(View view){
        mViews.clear();
        setContentView(view);
    }

    public void removeView(final View view){
        removeView(view, null, null);
    }

    public void removeView(final View view, Animation animation){
        removeView(view, animation, null);
    }

    public void removeView(final View view, Animation animation, final ViewManagerListener listener){
        if(view == null || mViews.size() < 2 || view != mViews.peek()){
            return;
        }

        mViews.pop();
        if(animation != null){
            view.setAnimation(animation);
            AnimationListenerProxy proxy = new AnimationListenerProxy(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    //notify.switchView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    addViewInRoot(mViews.peek(), null, true);
                    getRootView().removeView(view);
                    if(listener != null){
                        listener.onViewRemoved(view);
                    }
                }
            });
            animation.setAnimationListener(proxy);
            view.startAnimation(animation);
        }else{
            addViewInRoot(mViews.peek(), null, true);
            getRootView().removeView(view);
            if(listener != null){
                listener.onViewRemoved(view);
            }
        }
    }

    public void removeBackgroundView(View view){
        if(mViews.size() > 1 && mViews.contains(view) && mViews.peek() != view){
            mViews.remove(view);
        }
    }

    public boolean backToPreviousView(){
        return backToPreviousView(null);
    }

    public boolean backToPreviousView(Animation animation){
        return backToPreviousView(animation, null);
    }

    public boolean backToPreviousView(Animation animation, ViewManagerListener listener){
        if(mViews.size() < 2){
            return false;
        }

        removeView(mViews.peek(), animation, listener);
        return true;
    }

    public void backToFirstView(Animation animation){
        if(mViews.size() < 2){
            return;
        }

        if(mViews.size() > 2){
            View firstView = mViews.firstElement();
            View view = mViews.peek();
            mViews.clear();
            mViews.push(firstView);
            mViews.push(view);
            replacePreView();
        }

        backToPreviousView(animation);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(mViews.size() < 1){
            return false;
        }
        View sView = mViews.peek();
        if(sView != null && sView.onKeyDown(keyCode, event)){
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK){
            return backToPreviousView();
        }

        return false;
    }


    public void popWindow(){
        backToPreviousView();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event){
        if(mViews.size() < 1){
            return false;
        }
        View sView = mViews.peek();
        if(sView != null && sView.onKeyUp(keyCode, event)){
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }

        return false;
    }

    public interface ViewManagerListener{
        public void onViewAdded(View view);
        public void onViewRemoved(View view);
    }
}
