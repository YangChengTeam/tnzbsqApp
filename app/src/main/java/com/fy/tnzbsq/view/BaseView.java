package com.fy.tnzbsq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/24.
 */

public abstract class BaseView extends FrameLayout implements IView {
    public BaseView(Context context) {
        super(context);
        inflate(context, getLayoutId(), this);

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }

        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, getLayoutId(), this);

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }

        init();
    }

    @Override
    public void init() {

    }

}

