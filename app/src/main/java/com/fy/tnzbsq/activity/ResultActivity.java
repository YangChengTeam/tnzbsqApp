package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.NavgationBarUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.ShareActsDialog;
import com.fy.tnzbsq.view.ShareImageDialog;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.kk.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.fy.tnzbsq.util.NavgationBarUtils.checkDeviceHasNavigationBar;

public class ResultActivity extends BaseAppActivity {

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.back_img)
    ImageView backImg;

    @BindView(R.id.top_title)
    TextView titleNameTv;

    @BindView(R.id.result_layout)
    RelativeLayout resultLayout;

    @BindView(R.id.iv_result)
    ImageView mResultImageView;

    @BindView(R.id.share_layout_img)
    ImageView shareLayoutImg;

    @BindView(R.id.share_app_layout)
    LinearLayout shareLayout;

    public String url = "";

    private Bitmap bitmapShow;

    private Bitmap bitmapTemp;

    private String fileName = "";

    private File file;

    private CustomProgress dialog;

    private String imagePath = "";

    // 分享弹出窗口
    private SharePopupWindow shareWindow;

    private UMImage image;

    //图片后缀类型
    private String tempType = "";

    CustomProgress shareDialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/*"}, null);
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                    Toast.makeText(getApplicationContext(), "图片已保存到图库", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.result;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        image = new UMImage(context, R.mipmap.logo_share);

        // 创建图片存储的文件夹
        File fileDir = new File(Contants.BASE_IMAGE_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // 生成后标题
        if (bundle != null && bundle.getString("createTitle") != null && bundle.getString("createTitle").length() > 0) {
            String createTitle = intent.getExtras().getString("createTitle");
            titleNameTv.setText(createTitle);
        }

        // 获取图片的下载路径
        if (bundle != null && bundle.getString("imagePath") != null && bundle.getString("imagePath").length() > 0) {
            imagePath = intent.getExtras().getString("imagePath");
        }

        shareDialog = CustomProgress.create(context, "正在分享...", true, null);
        shareDialog.setTitle("装B神器分享");

        if (imagePath.indexOf(".") > -1) {
            tempType = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length());

            if (tempType.toLowerCase().equals("jpg") || tempType.toLowerCase().equals("png") || tempType.toLowerCase().equals("gif")) {
                fileName = Contants.BASE_IMAGE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + "." + tempType;
                fileName = Contants.BASE_IMAGE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + "." + tempType;
            }
        }

        Glide.with(this).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                bitmapShow = resource;
                mResultImageView.setImageBitmap(resource);
                mLoadingLayout.setVisibility(View.GONE);
                Logger.e("w-->" + resource.getWidth() + "---h-->" + resource.getHeight());
            }
        });

        Glide.with(this).load(imagePath).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    saveFileToSD(fileName, resource);
                } catch (Exception e) {
                    Logger.e("file load error --->");
                    e.printStackTrace();
                }
            }
        });
    }

    //往SD卡写入文件的方法
    public void saveFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            output.close();
            //关闭输出流
        } else {
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.back_img)
    public void back() {
        finish();
    }

    @OnClick(R.id.share_app_layout)
    public void shareApp() {
        shareWindow = new SharePopupWindow(context, itemsOnClick);
        shareWindow.showAtLocation(resultLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, NavgationBarUtils.getNavigationBarHeight(ResultActivity.this));
        backgroundAlpha(0.5f);
        shareWindow.setOnDismissListener(new PoponDismissListener());
    }

    @OnClick(R.id.share_layout_img)
    public void shareImage() {
        if (isValidContext(context)) {
            if (bitmapShow == null) {
                bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
            }
            ShareActsDialog shareActsDialog = new ShareActsDialog(context, bitmapShow, fileName, tempType);
            shareActsDialog.showShareDialog(shareActsDialog);
        }
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    // 弹出窗口监听消失
    public class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void ShareWeb(int thumb_img, SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(context, thumb_img);
        UMWeb web = new UMWeb("http://zs.qqtn.com");
        web.setThumb(thumb);
        web.setDescription("有人向你发起了装逼挑战，是否一战？");
        web.setTitle("装逼神器");
        new ShareAction((Activity) context).withMedia(web).setPlatform(platform).setCallback(umShareListener).share();
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_layout:
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qq_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.QQ);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qzone_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.QZONE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wechat_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.WEIXIN);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wxcircle_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.WEIXIN_CIRCLE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(shareDialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (shareDialog != null && shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            Log.d("plat", "platform" + platform);
            Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (shareDialog != null && shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (shareDialog != null && shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 保存图片
     */
    public void saveImage(String imageRealPath) {
        if (file == null) {
            file = new File(fileName);
        }

        if (!file.exists()) {
            // new ImageLoaderAsyncTask(2).execute();
        } else {
            if (bitmapShow == null) {
                bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.def_logo);
            }
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName,
                        null);

                // CapturePhotoUtils.insertImage(context.getContentResolver(),
                // bitmapShow, fileName, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    }

    public void toSave() {
        saveImage(imagePath);
    }

    /**
     * 分享功能
     */
    public void toShare() {
        if (file == null) {
            file = new File(fileName);
        }
        ToastUtil.toast(context, "toshare");
        if (bitmapShow == null) {
            bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.def_logo);
        }

        UMImage image = new UMImage(context, bitmapShow);

        ShareImageDialog shareDialog = new ShareImageDialog(context, image);
        shareDialog.showShareDialog(shareDialog);
    }

    /**
     * 点击图片后展示缩放图片
     * 格式为GIF时，不预览图片
     */

    public void imageShow(String path) {
        if (tempType != null && !tempType.toLowerCase().equals("gif")) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", fileName);
            //showActivity(ResultActivity.this, ImageShowActivity.class, bundle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapTemp != null) {
            bitmapTemp.recycle();
            bitmapTemp = null;
        }
    }

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

    protected void onActivityResult(int resultCode, int arg1, Intent arg2) {
        super.onActivityResult(resultCode, arg1, arg2);
        try {
            UMShareAPI.get(this).onActivityResult(resultCode, arg1, arg2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
