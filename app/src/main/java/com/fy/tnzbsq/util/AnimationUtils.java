package com.fy.tnzbsq.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;


/**
 * Created by zhangkai on 16/4/11.
 */
public class AnimationUtils {

    public static void itemAnimate(View paramView, int xdelta,int ydelta, int delay, int type)
    {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)paramView.getLayoutParams();
        layoutParams.leftMargin = xdelta;
        layoutParams.topMargin = ydelta;
        int width = paramView.getWidth();
        paramView.setLayoutParams(layoutParams);
        paramView.clearAnimation();
        AnimationSet localAnimationSet = new AnimationSet(true);
        localAnimationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
        if(type == 0) {
            localAnimationSet.addAnimation(new TranslateAnimation(-xdelta, 0.0f, width * 2 + (-ydelta), 0.0f));
        } else {
            localAnimationSet.addAnimation(new TranslateAnimation(width*3 - xdelta, 0.0f, width * 2 + (-ydelta), 0.0f));
        }
        localAnimationSet.setDuration(150L);
        localAnimationSet.setStartOffset(delay);
        localAnimationSet.setFillAfter(true);
        paramView.startAnimation(localAnimationSet);
    }
}
