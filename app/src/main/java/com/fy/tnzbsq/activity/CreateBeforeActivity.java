package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ImageCreateRet;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.bean.ZBDataFieldInfo;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.ScreenUtils;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.view.ChargeDialog;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.GlideCircleTransform;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.kk.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.utils.SystemTool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by admin on 2017/8/29.
 */

public class CreateBeforeActivity extends BaseAppActivity implements ChargeDialog.TimeListener {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.loading_iv)
    GifImageView mGifLoadingView;

    @BindView(R.id.iv_no_date)
    ImageView mNoDataView;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.image_layout)
    LinearLayout mImageLayout;

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

    private boolean isBuy = false;

    private CustomProgress dialog;

    private boolean isChooseImage;

    private int timeNum = 0;

    private Timer timer;

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
                        mCreateBgImageView.getLayoutParams().width = getRealWidth(resource.getWidth());
                        mCreateBgImageView.getLayoutParams().height = getRealWHeight(resource.getHeight());
                        mCreateBgImageView.setImageBitmap(resource);

                        mLoadingLayout.setVisibility(View.GONE);
                        Logger.e("w-->" + resource.getWidth() + "---h-->" + resource.getHeight());
                        createInputView();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mGifLoadingView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.VISIBLE);
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

        User user = (User) PreferencesUtils.getObject(context, "login_user", User.class);
        App.loginUser = user;
    }

    public int getRealWidth(int cWidth) {
        if (cWidth > ScreenUtils.getScreenWidth(this)) {
            return ScreenUtils.getScreenWidth(this) - SizeUtils.dp2px(this, 20);
        } else {
            return cWidth;
        }
    }

    public int getRealWHeight(int cHeight) {
        if (cHeight > ScreenUtils.getScreenHeight(this)) {
            return ScreenUtils.getScreenHeight(this) - SizeUtils.dp2px(this, 20);
        } else {
            return cHeight;
        }
    }

    @Override
    protected void initViews() {
        FrameLayout.LayoutParams iParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        iParams.setMargins(SizeUtils.dp2px(this, 6), SizeUtils.dp2px(this, 6), SizeUtils.dp2px(this, 6), SizeUtils.dp2px(this, 6));
        mImageLayout.setLayoutParams(iParams);

        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cParams.setMargins(0, SizeUtils.dp2px(this, 12), 0, 0);
        mCreateTypeLayout.setLayoutParams(cParams);
    }

    protected void createInputView() {

        if (mZbDataInfo != null && mZbDataInfo.field != null) {

            final int paddingLeft = SizeUtils.dp2px(this, 10);
            final int textSize = SizeUtils.sp2px(this, 6);
            final int tHeight = SizeUtils.dp2px(this, 38);
            final int tMargin = SizeUtils.dp2px(this, 42);

            Map<String, String> requestData = new HashMap<String, String>();
            Logger.e("create field --->" + mZbDataInfo.field.size());
            for (int i = 0; i < mZbDataInfo.field.size(); i++) {
                ZBDataFieldInfo zField = mZbDataInfo.field.get(i);
                if (zField.is_hide == 0) {

                    if (zField.input_type == 0) {
                        EditText wordTv = new EditText(this);
                        wordTv.setHint(zField.def_val);
                        wordTv.setBackgroundResource(R.drawable.input_bg);
                        wordTv.setPadding(paddingLeft, 0, 0, 0);
                        wordTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        wordTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(zField.text_len_limit)});

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
                        params.gravity = Gravity.CENTER;
                        params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10));
                        mCreateTypeLayout.addView(wordTv, params);
                    }
                    if (zField.input_type == 1) {

                        final LinearLayout customLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        customLayout.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams sParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        sParams.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10));
                        sParams.gravity = Gravity.CENTER;
                        final Spinner niceSpinner = new Spinner(this);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
                        adapter.setDropDownViewResource(R.layout.spinner_item_text);
                        final List<String> dataSet = new LinkedList<String>();

                        if (zField.select != null && zField.select.size() > 0) {
                            for (int j = 0; j < zField.select.size(); j++) {
                                //if (!zField.select.get(j).opt_text.equals("自定义文字")) {
                                dataSet.add(zField.select.get(j).opt_text);
                                //}
                            }
                        }
                        adapter.addAll(dataSet);
                        niceSpinner.setAdapter(adapter);
                        customLayout.addView(niceSpinner, sParams);

                        EditText customTv = new EditText(CreateBeforeActivity.this);
                        customTv.setHint("请输入文字");
                        customTv.setBackgroundResource(R.drawable.input_bg);
                        customTv.setPadding(paddingLeft, 0, 0, 0);
                        customTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        customTv.setVisibility(View.GONE);
                        customTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(zField.text_len_limit)});

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
                        params.gravity = Gravity.CENTER;
                        params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(CreateBeforeActivity.this, 10));
                        customLayout.addView(customTv, params);

                        mCreateTypeLayout.addView(customLayout, cParams);

                        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                niceSpinner.setContentDescription(dataSet.get(position));

                                if (dataSet.get(position).contains("自定义")) {
                                    for (int i = 0; i < customLayout.getChildCount(); i++) {
                                        if (customLayout.getChildAt(i) instanceof EditText) {
                                            customLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < customLayout.getChildCount(); i++) {
                                        if (customLayout.getChildAt(i) instanceof EditText) {
                                            customLayout.getChildAt(i).setVisibility(View.GONE);
                                        }
                                    }
                                }
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
                        imageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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
                } else {
                    EditText hideEv = new EditText(this);
                    hideEv.setVisibility(View.GONE);
                    LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    hParams.gravity = Gravity.CENTER;
                    mCreateTypeLayout.addView(hideEv, hParams);
                }
            }

            //生成按钮
            final Button createBtn = new Button(this);
            createBtn.setBackgroundResource(R.drawable.common_btn_selector);
            createBtn.setText("一键生成");
            createBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight + 6);
            btnParams.gravity = Gravity.CENTER;
            btnParams.setMargins(tMargin, SizeUtils.dp2px(this, 10), tMargin, SizeUtils.dp2px(this, 30));
            mCreateTypeLayout.addView(createBtn, btnParams);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createImage();
                }
            });
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (App.loginUser != null) {
            getUserIsBuy();
        }

        computeTime();
    }

    public void getUserIsBuy() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("goods_id", mZbDataInfo != null ? mZbDataInfo.id : "");
        params.put("user_id", App.loginUser.id + "");

        okHttpRequest.aget(Server.URL_USER_SOURCE_STATE, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response != null) {
                    try {
                        Result result = Contants.gson.fromJson(response, new TypeToken<Result>() {
                        }.getType());
                        if (result != null && result.errCode.equals("0")) {
                            isBuy = true;
                            return;
                        } else {
                            String sourceIdsKey = App.loginUser != null ? App.loginUser.id + "_ids" : App.ANDROID_ID + "_ids";
                            String sourceIds = PreferencesUtils.getString(context, sourceIdsKey);
                            if (!StringUtils.isEmpty(sourceIds)) {
                                String[] sourceArray = sourceIds.split(",");
                                if (isBuyVip(sourceArray, mZbDataInfo.id)) {
                                    isBuy = true;
                                    return;
                                }
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } finally {
                        String sourceIdsKey = App.loginUser != null ? App.loginUser.id + "_ids" : App.ANDROID_ID + "_ids";
                        String sourceIds = PreferencesUtils.getString(context, sourceIdsKey);
                        if (!StringUtils.isEmpty(sourceIds)) {
                            String[] sourceArray = sourceIds.split(",");
                            if (isBuyVip(sourceArray, mZbDataInfo.id)) {
                                isBuy = true;
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onBefore() {

            }
        });
    }

    public static boolean isBuyVip(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }


    @Override
    public void timeStart() {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeNum++;
                if (timeNum > 20) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    public void computeTime(){
        if (timeNum > 20) {
            String sourceIdsKey = App.loginUser != null ? App.loginUser.id + "_ids" : App.ANDROID_ID + "_ids";
            StringBuffer sourceIds = new StringBuffer(PreferencesUtils.getString(context, sourceIdsKey, ""));
            if (!StringUtils.isEmpty(sourceIds.toString())) {
                sourceIds.append(",");
            }

            sourceIds.append(mZbDataInfo.id);
            PreferencesUtils.putString(context, sourceIdsKey, sourceIds.toString());

            PreferencesUtils.putBoolean(context, "is_comment", true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        computeTime();

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
            isChooseImage = true;
            HeadImageUtils.imgResultPath = tempFile.getAbsolutePath();
            Glide.with(this).load(tempFile).transform(new GlideCircleTransform(context)).into(createSelectImageView);
        }
    }


    public void createImage() {

        Map<String, String> requestData = new HashMap<String, String>();
        if (mCreateTypeLayout != null) {
            boolean isValidate = true;
            for (int i = 0; i < mCreateTypeLayout.getChildCount(); i++) {
                if (mCreateTypeLayout.getChildAt(i) instanceof EditText) {
                    EditText iEditText = (EditText) mCreateTypeLayout.getChildAt(i);
                    if (StringUtils.isEmpty(iEditText.getText()) && iEditText.getVisibility() == View.VISIBLE) {
                        ToastUtil.toast(this, "请输入值");
                        isValidate = false;
                        break;
                    } else {
                        requestData.put(i + "", iEditText.getText().toString());
                    }
                    continue;
                }

                if (mCreateTypeLayout.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout tempLinearLayout = (LinearLayout) mCreateTypeLayout.getChildAt(i);
                    for (int m = 0; m < tempLinearLayout.getChildCount(); m++) {
                        if (tempLinearLayout.getChildAt(m) instanceof EditText) {
                            EditText iEditText = (EditText) tempLinearLayout.getChildAt(m);
                            if (StringUtils.isEmpty(iEditText.getText()) && iEditText.getVisibility() == View.VISIBLE) {
                                ToastUtil.toast(this, "请输入值");
                                isValidate = false;
                                break;
                            } else {
                                if (iEditText.getVisibility() == View.VISIBLE) {
                                    requestData.put(i + "", iEditText.getText().toString());
                                }
                            }
                            continue;
                        }

                        if (tempLinearLayout.getChildAt(m) instanceof Spinner) {
                            Spinner iSpinner = (Spinner) tempLinearLayout.getChildAt(m);
                            if (StringUtils.isEmpty(iSpinner.getContentDescription())) {
                                ToastUtil.toast(this, "请选择值");
                                isValidate = false;
                                break;
                            } else {
                                requestData.put(i + "", iSpinner.getContentDescription().toString());
                            }
                            continue;
                        }
                    }
                }

                if (mCreateTypeLayout.getChildAt(i) instanceof RelativeLayout) {
                    RelativeLayout tempLayout = (RelativeLayout) mCreateTypeLayout.getChildAt(i);
                    for (int j = 0; j < tempLayout.getChildCount(); j++) {
                        if (tempLayout.getChildAt(j) instanceof ImageView) {
                            requestData.put(i + "", "");
                            break;
                        }
                    }
                    continue;
                }
            }

            // 自定义事件,统计次数
            MobclickAgent.onEvent(CreateBeforeActivity.this, "create_click", SystemTool.getAppVersionName(this));

            if (isValidate) {

                boolean isAuth = false;
                if (mZbDataInfo.is_vip == 0) {
                    isAuth = true;
                } else {
                    if (App.loginUser == null) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        return;
                    } else {
                        if (App.loginUser.is_vip == 1) {
                            isAuth = true;
                        } else {
                            if (isBuy) {
                                isAuth = true;
                            }
                        }
                    }
                }

                if (!isAuth) {
                    ChargeDialog dialog = new ChargeDialog(CreateBeforeActivity.this, mZbDataInfo != null ? mZbDataInfo.id : "");
                    dialog.setTimeListener(this);
                    dialog.showChargeDialog(dialog);
                    return;
                }

                if (dialog == null) {
                    dialog = CustomProgress.create(context, "正在生成图片...", true, null);
                }

                dialog.show();

                Logger.e("create field json data--->" + JSON.toJSONString(requestData) + "file path--->" + HeadImageUtils.imgResultPath);

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", mZbDataInfo != null ? mZbDataInfo.id : "");
                params.put("mime", App.ANDROID_ID);

                if (requestData.size() > 0) {
                    params.put("requestData", JSON.toJSONString(requestData));
                }

                if (isChooseImage) {
                    okHttpRequest.aget(Server.URL_IMAGE_CREATE, params, new File(HeadImageUtils.imgResultPath), new OnResponseListener() {
                        @Override
                        public void onSuccess(String response) {

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            doResult(response);
                        }

                        @Override
                        public void onError(Exception e) {
                        }

                        @Override
                        public void onBefore() {
                        }
                    });
                } else {
                    okHttpRequest.aget(Server.URL_IMAGE_CREATE, params, new OnResponseListener() {
                        @Override
                        public void onSuccess(String response) {

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            doResult(response);
                        }

                        @Override
                        public void onError(Exception e) {
                        }

                        @Override
                        public void onBefore() {
                        }
                    });
                }
            }
        }
    }

    public void doResult(String response) {
        if (response != null) {
            Logger.e("create result --- >" + response);
            try {
                ImageCreateRet res = Contants.gson.fromJson(response, new TypeToken<ImageCreateRet>() {
                }.getType());
                if (Result.checkResult(CreateBeforeActivity.this, res)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("imagePath", res.data);
                    bundle.putString("createTitle", mZbDataInfo != null ? mZbDataInfo.title : "");

                    Intent intent = new Intent(CreateBeforeActivity.this, ResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateBeforeActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CreateBeforeActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeadImageUtils.imgPath = null;
        HeadImageUtils.imgResultPath = null;
    }
}
