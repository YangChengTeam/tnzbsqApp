package com.yc.loanbox.view.widget;

import android.content.Context;
import android.widget.TextView;

import com.yc.loanbox.R;
import com.yc.loanbox.R2;

import butterknife.BindView;

public class MyLoadingDialog extends BaseDialog {

    @BindView(R2.id.message)
    TextView messageTextView;

    public MyLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_loading;
    }

    public void show(String message) {
        messageTextView.setText(message);
        this.show();
    }

    @Override
    protected void initViews() {

    }
}
