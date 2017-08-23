package com.fy.tnzbsq.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.view.CapturePhotoUtils;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.DiskImageRequest;
import org.kymjs.kjframe.ui.BindView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageCropActivity extends BaseActivity {

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.cropImageView)
    public CropImageView cropImageView;

    @BindView(id = R.id.crop_confirm_btn, click = true)
    private ImageButton confirmBtn;

    @BindView(id = R.id.crop_cancel_btn, click = true)
    private ImageButton cancelBtn;

    private String imagePath = "";

    private ProgressDialog loadDialog;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    private String fileName = "temp_crop.jpg";

    @Override
    public void setRootView() {
        setContentView(R.layout.image_crop);
    }

    @Override
    public void initData() {
        super.initData();

        //清除历史路径数据
        HeadImageUtils.imgResultPath = null;

        titleNameTv.setText(getResources().getString(R.string.crop_image_text));

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.def_logo)
                .showImageOnFail(R.mipmap.def_logo)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(50))
                .build();

        if (HeadImageUtils.imgPath != null && HeadImageUtils.imgPath.length() > 0) {
            fileName = HeadImageUtils.imgPath.substring(HeadImageUtils.imgPath.lastIndexOf("/") + 1, HeadImageUtils.imgPath.length());

            //加载本地图片，需要在图片前面加上前缀 "file:///"
            imageLoader.displayImage("file:///" + HeadImageUtils.imgPath, cropImageView, options);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.getInt("xcrop") > 0 && bundle.getInt("ycrop") > 0) {
                cropImageView.setCustomRatio(bundle.getInt("xcrop"), bundle.getInt("ycrop"));
            } else {
                //设置自由裁剪
                cropImageView.setCropMode(CropImageView.CropMode.FREE);
            }
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.crop_confirm_btn:
                loadDialog = new ProgressDialog(ImageCropActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                loadDialog.setMessage("图片裁剪中...");
                if (isValidContext(context)) {
                    loadDialog.show();
                }
                cropImageView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
                break;
            case R.id.crop_cancel_btn:
                finish();
                break;
        }
    }


    public Uri createSaveUri() {
        return Uri.fromFile(new File(context.getExternalCacheDir(), "cropped"));
    }

    private CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            if (cropped != null) {
                HeadImageUtils.cropBitmap = cropped;
                //HeadImageUtils.cutPhoto = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), cropped, null,null));

                /*String urlString = CapturePhotoUtils.insertImage(context.getContentResolver(), cropped, fileName, null);
                if (urlString != null && urlString.length() > 0) {
                    Toast.makeText(context, "3333", Toast.LENGTH_SHORT).show();
                    HeadImageUtils.cutPhoto = Uri.parse(urlString);
                }*/
            }
        }

        @Override
        public void onError() {
            if (isValidContext(context) && loadDialog != null && loadDialog.isShowing()) {
                loadDialog.dismiss();
            }
            finish();
        }
    };

    private SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {

            /*if (outputUri != null) {
                //Log.e("outputUri is not null","outputUri is not null---");

                try {
                    HeadImageUtils.cropBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
                    Toast.makeText(context, "not null"+ HeadImageUtils.cropBitmap.getWidth(), Toast.LENGTH_SHORT).show();
                    cancelBtn.setImageBitmap(HeadImageUtils.cropBitmap);
                    HeadImageUtils.cutPhoto = outputUri;
                    Toast.makeText(context, HeadImageUtils.cropBitmap +"", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("outputUri null-----"," outputUri null-----");
                Toast.makeText(context, "uri null", Toast.LENGTH_SHORT).show();
            }*/

            /*if (!TextUtils.isEmpty(outputUri.getAuthority())) {
                Cursor cursor = getContentResolver().query(outputUri,
                        new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (null == cursor) {
                    Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();
                HeadImageUtils.imgResultPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                Toast.makeText(context, "4444444", Toast.LENGTH_SHORT).show();
            } else {
                //HeadImageUtils.imgResultPath = outputUri.getPath();
                Toast.makeText(context, "555555", Toast.LENGTH_SHORT).show();
            }*/

            if (isValidContext(context) && loadDialog != null && loadDialog.isShowing()) {
                loadDialog.dismiss();
            }

            Intent intent = new Intent();
            setResult(HeadImageUtils.FREE_CUT, intent);
            finish();
        }

        @Override
        public void onError() {
            if (isValidContext(context) && loadDialog != null && loadDialog.isShowing()) {
                loadDialog.dismiss();
            }
            finish();
        }
    };

    @SuppressLint("NewApi")
    private boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
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
        //HeadImageUtils.imgPath = null;
    }
}
