package com.yc.loanbox.view.widget;

import android.content.Context;
import android.widget.ImageView;

import com.jakewharton.rxbinding3.view.RxView;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.helper.GlideHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class ActiveDialog extends BaseDialog {

    @BindView(R2.id.active_close_btn)
    ImageView closeImageView;

    @BindView(R2.id.active_image)
    public ImageView activeImage;

    public ActiveDialog(Context context) {
        super(context);

        RxView.clicks(closeImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            dismiss();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_dialog_active;
    }

    public void setImageUrl(String url){
        GlideHelper.imageView(getContext(), activeImage, url, 0);
    }

    @Override
    public void show() {
        if(!isShowing()) {
            super.show();
        }
    }

    @Override
    protected void initViews() {

    }
}
