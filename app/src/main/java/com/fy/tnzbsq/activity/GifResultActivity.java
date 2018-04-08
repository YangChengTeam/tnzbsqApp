package com.fy.tnzbsq.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.util.Utils;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.ShareFightDialog;
import com.fy.tnzbsq.view.ShareGifDialog;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifOptions;
import rx.functions.Action1;

public class GifResultActivity extends BaseAppActivity {

    @BindView(R.id.back_img)
    ImageView backImg;

    @BindView(R.id.top_title)
    TextView titleNameTv;

    @BindView(R.id.iv_result)
    GifImageView gifResultImageView;

    @BindView(R.id.result_layout)
    RelativeLayout resultLayout;

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

    private Dialog tipDialog;

    CustomProgress shareDialog;

    private File resultFile;

    ProgressDialog gifLoadDialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/*"},
                            null);
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + file.getAbsolutePath())));
                    // scanDirAsync(context,FileUtils.getDiskCacheDir(context));
                    Toast.makeText(getApplicationContext(), "图片已保存到图库", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.gif_result;
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
        if (bundle != null && bundle.getString("title") != null) {
            titleNameTv.setText(bundle.getString("title"));
        }

        // 获取图片的下载路径
        if (bundle != null && bundle.getString("imagePath") != null && bundle.getString("imagePath").length() > 0) {
            imagePath = intent.getExtras().getString("imagePath");
        }

        Logger.i("ip---->" + imagePath);

        //Glide.with(this).load("http://img.soogif.com/FQZkOfZdmZ44amInzOJRs7cAPF9ZDzwS.gif").asGif().into(gifResultImageView);

        gifLoadDialog = new ProgressDialog(this);
        gifLoadDialog.setTitle("Gif图加载");
        gifLoadDialog.setMessage("加载中···");
        gifLoadDialog.show();

        Glide.with(this).load(imagePath).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                if (Utils.isValidContext(GifResultActivity.this) && gifLoadDialog != null && gifLoadDialog.isShowing()) {
                    gifLoadDialog.dismiss();
                }
                try {
                    resultFile = resource;

                    GifDrawable gifFromPath = new GifDrawable(resource);
                    gifResultImageView.setImageDrawable(gifFromPath);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (Utils.isValidContext(GifResultActivity.this) && gifLoadDialog != null && gifLoadDialog.isShowing()) {
                    gifLoadDialog.dismiss();
                }
            }
        });

        shareDialog = CustomProgress.create(context, "正在分享...", true, null);
        shareDialog.setTitle("装B神器分享");

        //首次加载制图页面时，提示用户如何操作
        tipDialog = new Dialog(this, R.style.Dialog_Fullscreen);
        tipDialog.setContentView(R.layout.result_dialog_tip);
        TextView iknowTv = (TextView) tipDialog.findViewById(R.id.confirm_img);
        iknowTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        iknowTv.getPaint().setAntiAlias(true);//抗锯齿

        iknowTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isValidContext(context) && tipDialog != null && tipDialog.isShowing()) {
                    //tipDialog.dismiss();
                }
            }
        });


        RxView.clicks(backImg).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(shareLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shareWindow = new SharePopupWindow(context, itemsOnClick);
                shareWindow.showAtLocation(resultLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(GifResultActivity.this));
                backgroundAlpha(0.5f);
                shareWindow.setOnDismissListener(new PoponDismissListener());
            }
        });
        RxView.clicks(shareLayoutImg).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (Utils.isValidContext(context)) {
                    if (bitmapShow == null) {
                        bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
                    }

                    /*ShareActsDialog shareActsDialog = new ShareActsDialog(context, bitmapShow, fileName, tempType);
                    shareActsDialog.showShareDialog(shareActsDialog);*/
                    if (resultFile.exists()) {

                        File fileDir = new File(Contants.BASE_FIGHT_IMAGE_DIR);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }

                        fileName = System.currentTimeMillis() + (int) (Math.random() * 10000) + ".gif";
                        File newFile = new File(fileDir, fileName);

                        boolean isSuccess = FileUtils.copyFile(resultFile, newFile, null);
                        if (isSuccess && newFile.exists()) {
                            ShareGifDialog shareGifDialog = new ShareGifDialog(context, newFile.getAbsolutePath());
                            shareGifDialog.showShareDialog(shareGifDialog);
                        }
                    }
                }
            }
        });
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
        web.setDescription("2018开启新年装逼新玩法，腾小牛在这里等你来挑战！");
        web.setTitle("装逼神器@你，并向你发起了装逼挑战！");
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

    protected void onActivityResult(int resultCode, int arg1, Intent arg2) {
        super.onActivityResult(resultCode, arg1, arg2);
        try {
            UMShareAPI.get(this).onActivityResult(resultCode, arg1, arg2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(Utils.isValidContext(GifResultActivity.this) && gifLoadDialog != null && gifLoadDialog.isShowing()){
            gifLoadDialog.dismiss();
        }

        if (bitmapShow != null) {
            bitmapShow.recycle();
            bitmapShow = null;
        }
        if (bitmapTemp != null) {
            bitmapTemp.recycle();
            bitmapTemp = null;
        }
    }
}
