package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.StringUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by admin on 2017/11/28.
 */

public class CommentDialog extends DialogFragment implements MyEditText.BackListener {

    private Context mContext;

    private Dialog dialog;

    Handler handler;

    private MyEditText mInputEditText;

    private TextView mSendTextView;

    private CustomProgress progress;

    public CommentDialog() {
    }

    @SuppressLint("ValidFragment")
    public CommentDialog(Context context) {
        this.mContext = context;
    }

    public interface SendBackListener {
        void sendContent(String content);
    }

    private SendBackListener sendBackListener;

    public void setSendBackListener(SendBackListener sendBackListener) {
        this.sendBackListener = sendBackListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View contentView = View.inflate(getActivity(), R.layout.comment_input_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        handler = new Handler();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Logger.e("dialog 1111--->");
                    dialog.dismiss();
                }
                return false;
            }
        });

        mInputEditText = (MyEditText) contentView.findViewById(R.id.et_comment);
        mSendTextView = (TextView) contentView.findViewById(R.id.tv_send_commit);
        mSendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mInputEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progress = CustomProgress.create(mContext, "发布中···", true, null);
                    progress.show();

                    sendBackListener.sendContent(mInputEditText.getText().toString());
                }
            }
        });

        mInputEditText.setFocusable(true);
        mInputEditText.setFocusableInTouchMode(true);
        mInputEditText.requestFocus();

        mInputEditText.setBackListener(this);

        return dialog;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("dialog 2222--->");
                hideSoftKeyboard();
            }
        }, 200);
    }

    public void hideSoftKeyboard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            Logger.e(e.getMessage());
        }
    }

    public void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void back() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
