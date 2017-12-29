package com.fy.tnzbsq.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.view.GlideCircleTransform;

/**
 * Created by admin on 2017/12/28.
 */

public class CardWindowFragment extends DialogFragment {

    Button createBtn;

    ImageView closeImageView;

    ImageView mCreateBgImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_window_dialog, container);

        createBtn = (Button) view.findViewById(R.id.btn_create);
        closeImageView = (ImageView) view.findViewById(R.id.iv_close);
        mCreateBgImageView = (ImageView) view.findViewById(R.id.iv_create_bg);

        Glide.with(this).load(R.mipmap.card_icon).transform(new GlideCircleTransform(getActivity())).into(mCreateBgImageView);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setWindowAnimations(R.style.my_popwindow_anim_style);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                PreferencesUtils.putBoolean(getActivity(), "show_card", false);
                Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                startActivity(intent);
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putBoolean(getActivity(), "show_card", false);
                dismiss();
            }
        });

        return view;
    }
}
