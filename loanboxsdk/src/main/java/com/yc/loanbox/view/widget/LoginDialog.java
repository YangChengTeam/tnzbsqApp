package com.yc.loanbox.view.widget;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yc.loanbox.R;
import com.yc.loanbox.R2;

import butterknife.BindView;


public class LoginDialog extends BaseDialog {

    @BindView(R2.id.ed_phone)
    public EditText phoneEditText;

    @BindView(R2.id.ed_yzm)
    public TextView yzmEditText;

    @BindView(R2.id.get_yzm)
    public TextView yzmTextView;

    @BindView(R2.id.login_btn)
    public Button loginBtn;

    public LoginDialog(Context context) {
        super(context);
        this.setCancelable(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_dialog_login;
    }

    @Override
    protected void initViews() {

    }
}
