package com.fy.tnzbsq.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.yc.loanboxsdk.LoanboxSDK;

/**
 * Created by admin on 2017/12/28.
 */

public class CardWindowFragment extends DialogFragment {

    ImageView closeImageView;

    ImageView mLuckBgImageView;

    public String popImageUrl;

    interface AdDismissListener{
        void adDismiss();
    }

    public AdDismissListener adDismissListener;

    public void setAdDismissListener(AdDismissListener adDismissListener) {
        this.adDismissListener = adDismissListener;
    }

    public void setPopImageUrl(String popImageUrl) {
        this.popImageUrl = popImageUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_window_dialog, container);

        closeImageView = (ImageView) view.findViewById(R.id.iv_close);
        mLuckBgImageView = (ImageView) view.findViewById(R.id.iv_luck);

        Glide.with(getActivity()).load(popImageUrl).error(R.mipmap.luck_draw_bg).into(mLuckBgImageView);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setWindowAnimations(R.style.my_popwindow_anim_style);

        mLuckBgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                PreferencesUtils.putBoolean(getActivity(), "show_card", false);
                App.showFloat = true;

                if (StringUtils.isEmpty(App.adUrl) && LoanboxSDK.defaultLoanboxSDK() != null) {
                    LoanboxSDK.defaultLoanboxSDK().open();
                } else {
                    Intent intent = new Intent(getActivity(), AdActivity.class);
                    startActivity(intent);
                }
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        PreferencesUtils.putBoolean(getActivity(), "show_card", false);
        adDismissListener.adDismiss();
        super.onDismiss(dialog);
    }
}
