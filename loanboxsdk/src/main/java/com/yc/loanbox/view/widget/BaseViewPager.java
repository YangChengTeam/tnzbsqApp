package com.yc.loanbox.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BaseViewPager extends ViewPager {
    private boolean scrollable = true;

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean enable) {
        this.scrollable = enable;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.scrollable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
}
