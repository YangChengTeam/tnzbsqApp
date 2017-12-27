package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.view.CapturePhotoUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.fy.tnzbsq.view.ShareActsDialog;
import com.fy.tnzbsq.view.ShareImageDialog;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.PreferenceHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends BaseActivity implements CustomWebViewDelegate {

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.webview)
    private CustomWebView customWebView;

	/*
     * @BindView(id = R.id.save_btn, click = true) private Button saveBtn;
	 *
	 * @BindView(id = R.id.share_btn, click = true) private Button shareBtn;
	 */

    @BindView(id = R.id.result_layout)
    private LinearLayout resultLayout;

    @BindView(id = R.id.share_layout_img, click = true)
    private ImageView shareLayoutImg;

    @BindView(id = R.id.share_app_layout, click = true)
    private LinearLayout shareLayout;

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

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    public void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.result);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        super.initData();
        customWebView.delegate = this;

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

        customWebView.delegate = this;
        customWebView.loadUrl("file:///android_asset/sch.html");

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
                if (isValidContext(context) && tipDialog != null && tipDialog.isShowing()) {
                    //tipDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);

        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
		/*
		 * case R.id.save_btn: saveImage(imagePath); break; case R.id.share_btn:
		 * if (file == null) { file = new File(fileName); }
		 *
		 * if (bitmapShow == null) { bitmapShow =
		 * BitmapFactory.decodeResource(getResources(), R.drawable.def_logo); }
		 *
		 * UMImage image = new UMImage(context, bitmapShow);
		 *
		 * ShareImageDialog shareDialog = new ShareImageDialog(context, image);
		 * shareDialog.showShareDialog(shareDialog);
		 *
		 * break;
		 */

            case R.id.share_app_layout:
                shareWindow = new SharePopupWindow(context, itemsOnClick);
                shareWindow.showAtLocation(resultLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(this));
                backgroundAlpha(0.5f);
                shareWindow.setOnDismissListener(new PoponDismissListener());
                break;
            case R.id.share_layout_img:
                //customWebView.loadUrl("javascript:closetishi();");

                if (isValidContext(context)) {
                    if (bitmapShow == null) {
                        bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
                    }

                    ShareActsDialog shareActsDialog = new ShareActsDialog(context, bitmapShow, fileName,tempType);
                    shareActsDialog.showShareDialog(shareActsDialog);
                }

                break;
            default:
                break;
        }
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

    private void ShareWeb(int thumb_img,SHARE_MEDIA platform){
        UMImage thumb = new UMImage(context,thumb_img);
        UMWeb web = new UMWeb("http://zs.qqtn.com");
        web.setThumb(thumb);
        web.setDescription("圣诞帽来啦，快来邀请好友一起玩吧");
        web.setTitle("别@官方了，快来装逼神器定制你的圣诞节帽子吧！");
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
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.QQ);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qzone_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.QZONE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wechat_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.WEIXIN);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wxcircle_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.WEIXIN_CIRCLE);
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
            if(shareDialog != null && shareDialog.isShowing()){
                shareDialog.dismiss();
            }
            Log.d("plat", "platform" + platform);
            Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(shareDialog != null && shareDialog.isShowing()){
                shareDialog.dismiss();
            }
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if(shareDialog != null && shareDialog.isShowing()){
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

    @Override
    public void Horizontal() {

    }

    @Override
    public void networkSet() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void setTitle(String title) {
        titleNameTv.setText(title);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void startFullActivity(GameInfo gameInfo) {

    }

    @Override
    public void setUrl(String url) {
        if (!url.contains("no-wifi")) {
            this.url = url;
        }
    }

    @Override
    public void showWheelView() {

    }

    @Override
    public void createImage(String id, String data,String isVip,String price) {
    }

    /**
     * 保存图片
     */
    @Override
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

    @Override
    public void addKeep(String id) {

    }

    @Override
    public void updateVersion() {
    }

    @Override
    public void selectPic(int xvalue, int yvalue) {
    }

    @Override
    public void submitMesage(String str, String description) {
    }

    @Override
    public void clearCache() {
    }

    @Override
    public void toSave() {
        saveImage(imagePath);
    }

    /**
     * 分享功能
     */
    @Override
    public void toShare() {
        if (file == null) {
            file = new File(fileName);
        }
        ToastUtil.toast(context,"toshare");
        if (bitmapShow == null) {
            bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.def_logo);
        }

        UMImage image = new UMImage(context, bitmapShow);

        ShareImageDialog shareDialog = new ShareImageDialog(context, image);
        shareDialog.showShareDialog(shareDialog);
    }

    /**
     * 异步加载网络图片并保存到本地
     */
    public class ImageLoaderAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {
        private int loadType;

        public ImageLoaderAsyncTask(int loadType) {
            super();
            this.loadType = loadType;
        }

        @Override
        protected void onPreExecute() {
            if (dialog == null) {
                dialog = CustomProgress.create(context, "图片加载中...", true, null);
            }
            dialog.setCanceledOnTouchOutside(false);
            if (isValidContext(context) && dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {

            file = new File(fileName);// 保存文件,
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    byte[] data = readInputStream(getRequest(imagePath));
                    bitmapTemp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bitmapTemp.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
                    return bitmapTemp;
                } catch (Exception e) {
                    return null;
                }
            } else {
                bitmapTemp = BitmapFactory.decodeFile(fileName);
            }

            if (loadType == 2) {
                // 其次把文件插入到系统图库
                try {
                    if (file != null && file.exists() && fileName != null) {
                        // MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        // file.getAbsolutePath(),fileName, null);
                        if (bitmapTemp == null) {
                            bitmapTemp = BitmapFactory.decodeResource(getResources(), R.mipmap.def_logo);
                        }
                        CapturePhotoUtils.insertImage(context.getContentResolver(), bitmapTemp, fileName, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
            return bitmapTemp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result != null) {
                // file = new File(fileName);

                bitmapShow = result;

                boolean isFirst = PreferenceHelper.readBoolean(ResultActivity.this, Contants.FIRST_SHARE_DATA,
                        Contants.IS_FIRST_SHARE, true);
                if (isFirst) {
                    PreferenceHelper.write(ResultActivity.this, Contants.FIRST_SHARE_DATA, Contants.IS_FIRST_SHARE,
                            false);
                    // 首次生成后，给用户提示
                    //customWebView.loadUrl("javascript:onetishi();");
                    //tipDialog.show();
                }
            }

            if (file.exists() && fileName != null) {
                customWebView.loadUrl("javascript:setImg('" + fileName + "');");
            }
        }
    }

    public static InputStream getRequest(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // 5秒
        if (conn.getResponseCode() == 200) {
            return conn.getInputStream();
        }
        return null;

    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 点击图片后展示缩放图片
     * 格式为GIF时，不预览图片
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void imageShow(String path) {
        if (tempType != null && !tempType.toLowerCase().equals("gif")) {
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", fileName);
            showActivity(ResultActivity.this, ImageShowActivity.class, bundle);
        }
    }

    // TODO

    /**
     * 页面加载完成之后再请求网络加载图片数据
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void initWithUrl(String url) {
        // 异步加载图片
        // new ImageLoaderAsyncTask(1).execute();
        if (imagePath.indexOf(".") > -1) {
            tempType = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length());

            if (tempType.toLowerCase().equals("jpg") || tempType.toLowerCase().equals("png") || tempType.toLowerCase().equals("gif")) {
                KJBitmap kjb = new KJBitmap();
                fileName = Contants.BASE_IMAGE_DIR + File.separator + System.currentTimeMillis()
                        + (int) (Math.random() * 10000) + "." + tempType;

                file = new File(fileName);// 保存文件,
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                kjb.saveImage(context, imagePath, fileName, true, new HttpCallBack() {

                    @Override
                    public void onPreStart() {
                        super.onPreStart();
                        if (dialog == null) {
                            dialog = CustomProgress.create(context, "图片加载中...", true, null);
                        }
                        dialog.setCanceledOnTouchOutside(false);
                        if (isValidContext(context) && dialog != null) {
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(byte[] t) {
                        super.onSuccess(t);

                        if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        if (file.exists()) {
                            bitmapShow = BitmapFactory.decodeFile(fileName);
                            boolean isFirst = PreferenceHelper.readBoolean(ResultActivity.this,
                                    Contants.FIRST_SHARE_DATA, Contants.IS_FIRST_SHARE, true);
                            if (isFirst) {
                                PreferenceHelper.write(ResultActivity.this, Contants.FIRST_SHARE_DATA,
                                        Contants.IS_FIRST_SHARE, false);
                                // 首次生成后，给用户提示
                                //customWebView.loadUrl("javascript:onetishi();");
                                if (isValidContext(context) && tipDialog != null){
                                    //tipDialog.show();
                                }
                            }

                            if (fileName != null && file.exists()) {
                                customWebView.loadUrl("javascript:setImg('" + fileName + "');");
                            }
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });

                // kjb.saveImage(context, imagePath, fileName);
                // customWebView.loadUrl("javascript:setImg('" + fileName +
                // "');");
            }
            if (tempType.equals("gif")) {

            }
        }
    }

    @Override
    public void updateUserName(String userName) {
    }

    @Override
    public void setShowState(boolean flag) {
    }

    @Override
    public void loadImageList(int currentPage) {
    }

    @Override
    public void search(String keyword) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		/*if (bitmapShow != null) {
			bitmapShow.recycle();
			bitmapShow = null;
		}*/
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

    @Override
    public void photoGraph() {
    }

    @Override
    public void fightMenu() {
        // TODO Auto-generated method stub

    }
}
