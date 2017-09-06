package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ZBDataFieldInfo;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.view.GlideCircleTransform;
import com.jaeger.library.StatusBarUtil;
import com.kk.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by admin on 2017/8/29.
 */

public class CreateBeforeActivity extends BaseAppActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.iv_create_bg_iv)
    ImageView mCreateBgImageView;

    @BindView(R.id.layout_create_type)
    LinearLayout mCreateTypeLayout;

    OKHttpRequest okHttpRequest = null;

    private ZBDataInfo mZbDataInfo;

    ImageView createSelectImageView;

    List<String> mSelectedImages;

    private int cWidth;

    private int cHeight;

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
            if (mZbDataInfo != null) {
                mToolbar.setTitle(mZbDataInfo.title);

                Glide.with(this).load(mZbDataInfo.front_img).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mCreateBgImageView.setImageBitmap(resource);
                        mLoadingLayout.setVisibility(View.GONE);
                        Logger.e("w-->" + resource.getWidth() + "---h-->" + resource.getHeight());
                    }
                });
            }
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

        if (mZbDataInfo != null && mZbDataInfo.field != null) {

            int paddingLeft = SizeUtils.dp2px(this, 10);
            int textSize = SizeUtils.sp2px(this, 6);
            int tHeight = SizeUtils.dp2px(this, 38);
            int tMargin = SizeUtils.dp2px(this, 42);

            for (int i = 0; i < mZbDataInfo.field.size(); i++) {
                ZBDataFieldInfo zField = mZbDataInfo.field.get(i);
                if (zField.is_hide == 0) {

                    if (zField.input_type == 0) {
                        EditText wordTv = new EditText(this);
                        wordTv.setHint(zField.def_val);
                        wordTv.setBackgroundResource(R.drawable.input_bg);
                        wordTv.setPadding(paddingLeft, 0, 0, 0);
                        wordTv.setTextSize(textSize);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
                        params.gravity = Gravity.CENTER;
                        params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10));
                        mCreateTypeLayout.addView(wordTv, params);
                    }
                    if (zField.input_type == 1) {

                        LinearLayout.LayoutParams sParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        sParams.setMargins(tMargin + SizeUtils.dp2px(this, 10), 0, tMargin, SizeUtils.dp2px(this, 10));
                        sParams.gravity = Gravity.CENTER;
                        Spinner niceSpinner = new Spinner(this);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
                        adapter.setDropDownViewResource(R.layout.spinner_item_text);
                        final List<String> dataSet = new LinkedList<String>();

                        if (zField.select != null && zField.select.size() > 0) {
                            for (int j = 0; j < zField.select.size(); j++) {
                                dataSet.add(zField.select.get(j).opt_text);
                            }
                        }
                        adapter.addAll(dataSet);
                        niceSpinner.setAdapter(adapter);
                        mCreateTypeLayout.addView(niceSpinner, sParams);

                        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ToastUtil.toast(CreateBeforeActivity.this, dataSet.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    if (zField.input_type == 2 || zField.input_type == 3 || zField.input_type == 4) {

                        cWidth = zField.x2 - zField.x1;
                        cHeight = zField.y2 - zField.y1;

                        RelativeLayout imageLayout = new RelativeLayout(this);
                        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                        TextView imageText = new TextView(this);
                        imageText.setText("选择图片：");
                        imageText.setTextSize(SizeUtils.sp2px(this, 6));
                        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        leftParams.setMargins(tMargin, SizeUtils.dp2px(this, 12), 0, 0);
                        imageLayout.addView(imageText, leftParams);

                        createSelectImageView = new ImageView(this);
                        createSelectImageView.setBackgroundResource(R.mipmap.create_select_img_icon);
                        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        rightParams.setMargins(0, SizeUtils.dp2px(this, 12), tMargin, 0);
                        imageLayout.addView(createSelectImageView, rightParams);

                        mCreateTypeLayout.addView(imageLayout, ivParams);

                        createSelectImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(true)
                                        .setShowGif(true)
                                        .setPreviewEnabled(false)
                                        .start(CreateBeforeActivity.this, PhotoPicker.REQUEST_CODE);
                            }
                        });
                    }
                }
            }

            //生成按钮
            Button createBtn = new Button(this);
            createBtn.setBackgroundResource(R.drawable.common_btn_selector);
            createBtn.setText("一键生成");
            createBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
            btnParams.gravity = Gravity.CENTER;
            btnParams.setMargins(tMargin, SizeUtils.dp2px(this, 10), tMargin, SizeUtils.dp2px(this, 30));
            mCreateTypeLayout.addView(createBtn, btnParams);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mSelectedImages = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (mSelectedImages != null && mSelectedImages.size() > 0) {
                    //Glide.with(this).load(mSelectedImages.get(0)).into(createSelectImageView);
                    HeadImageUtils.imgPath = mSelectedImages.get(0);
                    Intent intent = new Intent(CreateBeforeActivity.this, ImageCropActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("xcrop", cWidth);
                    bundle.putInt("ycrop", cHeight);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, HeadImageUtils.FREE_CUT);
                }
            }
        }

        if (requestCode == HeadImageUtils.FREE_CUT && HeadImageUtils.cropBitmap != null) {

            String fileName = Contants.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
            File fileDir = new File(Contants.BASE_NORMAL_FILE_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File tempFile = new File(fileName);
            try {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    HeadImageUtils.cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            HeadImageUtils.imgResultPath = tempFile.getAbsolutePath();
            Glide.with(this).load(tempFile).transform(new GlideCircleTransform(context)).into(createSelectImageView);
        }
    }
}
