package com.fy.tnzbsq.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Created by admin on 2017/8/29.
 */

public class CreateBeforeActivity extends BaseAppActivity {

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.iv_create_bg_iv)
    ImageView mCreateBgImageView;

    OKHttpRequest okHttpRequest = null;

    private ZBDataInfo mZbDataInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_before;
    }

    @Override
    protected void initVars() {
        okHttpRequest = new OKHttpRequest();

        StatusBarUtil.setColor(CreateBeforeActivity.this, getResources().getColor(R.color.colorAccent), 0);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mZbDataInfo = (ZBDataInfo) bundle.getSerializable("zb_data_info");
            mToolbar.setTitle(mZbDataInfo.title);

            Glide.with(this).load(mZbDataInfo.front_img).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mCreateBgImageView.setImageBitmap(resource);
                    mLoadingLayout.setVisibility(View.GONE);
                    Logger.e("w-->" + resource.getWidth() + "---h-->" + resource.getHeight());
                }
            });
        } else {
            mToolbar.setTitle("素材制作");
        }

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
