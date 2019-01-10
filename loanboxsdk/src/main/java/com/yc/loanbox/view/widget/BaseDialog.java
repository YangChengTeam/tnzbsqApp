package com.yc.loanbox.view.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;

import com.yc.loanbox.R;

import butterknife.ButterKnife;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutId(), null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(true);

        initViews();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    @Override
    public void dismiss() {
        try{
            super.dismiss();
        }catch (Exception e){}
    }
}
