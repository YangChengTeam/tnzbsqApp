package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.ImageUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;

import org.kymjs.kjframe.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ShareFightDialog extends Dialog {

    private Context context;

    private Dialog dialog;

    private Bitmap showBitmap;

    private Bitmap showBitmapThumb;

    //private Bitmap dialogBitmap;

    private UMImage image;

    private UMImage imageThumb;

    private String fileName;

    private File file;

    public ByteArrayOutputStream bitStream;

    private String filePath;

    private String tempType = "jpg";

    private ShareAction qqShareAction;

    private ShareAction weixinShareAction;

    private CustomProgress shareDialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (filePath != null && filePath.length() > 0 && file.exists()) {
                        Toast.makeText(context, "图片已保存到图库", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public ShareFightDialog(Context context, Bitmap bitmap) {
        super(context, R.style.Dialog);
        this.context = context;

        if (bitmap != null) {

            this.showBitmap = bitmap;
            this.image = new UMImage(context, bitmap);

            this.showBitmapThumb = ImageUtil.compressImage(bitmap, 18);//将图片压缩到32K以内，才能发送微信表情
            imageThumb = new UMImage(context,showBitmapThumb);
            Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
            Tiny.getInstance().source(showBitmap).asBitmap().withOptions(options).compress(new BitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap) {
                    showBitmap = bitmap;
                }
            });
            //int dialogImageHeight = DensityUtils.dip2px(context, 200);

            //double tempPro = (double) showBitmap.getHeight() / (double) showBitmap.getWidth();
            //double newWidth = ((double) dialogImageHeight) / tempPro;

            //dialogBitmap = ImageUtil.ZoomImg(showBitmap, (int) newWidth, dialogImageHeight);
        } else {
            //dialogBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_fight_default);
        }
    }

    public ShareFightDialog(final Context context, Bitmap bitmap, String filePath) {
        super(context, R.style.Dialog);
        this.context = context;
        if (bitmap != null) {
            this.showBitmap = bitmap;
            image = new UMImage(context, showBitmap);
            this.filePath = filePath;
            this.image = new UMImage(context, showBitmap);

            if (filePath != null) {
                file = new File(filePath);
            }

            this.showBitmapThumb = ImageUtil.compressImage(bitmap, 18);//将图片压缩到32K以内，才能发送微信表情
            imageThumb = new UMImage(context,showBitmapThumb);
            Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
            Tiny.getInstance().source(showBitmap).asBitmap().withOptions(options).compress(new BitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap) {
                    showBitmap = bitmap;
                }
            });
        }
    }

    public ShareFightDialog(Context context, String filePath) {
        super(context, R.style.Dialog);
        this.context = context;
        if (filePath != null && filePath.length() > 0) {

            this.filePath = filePath;

            if (filePath != null) {
                file = new File(filePath);
            }

            try {
                InputStream in = new FileInputStream(file);
                this.image = new UMImage(context, FileUtils.input2byte(in));

                imageThumb = image;

            } catch (Exception e) {
                e.printStackTrace();
            }

            //int dialogImageHeight = DensityUtils.dip2px(context, 200);

            //double tempPro = (double) showBitmap.getHeight() / (double) showBitmap.getWidth();
            //double newWidth = ((double) dialogImageHeight) / tempPro;

            //dialogBitmap = ImageUtil.ZoomImg(showBitmap, (int) newWidth, dialogImageHeight);
        } else {
            //dialogBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_fight_default);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.share_fight, null);
        setContentView(view);

        RelativeLayout wechatIv = (RelativeLayout) view.findViewById(R.id.wechat_layout);
        RelativeLayout qqonIv = (RelativeLayout) view.findViewById(R.id.qq_layout);
        RelativeLayout localIv = (RelativeLayout) view.findViewById(R.id.local_layout);

        GifImageView showImg = (GifImageView) view.findViewById(R.id.share_fight_img);

        LinearLayout cancelLayout = (LinearLayout) view.findViewById(R.id.cancel_layout);

        wechatIv.setOnClickListener(new clickListener());
        qqonIv.setOnClickListener(new clickListener());
        localIv.setOnClickListener(new clickListener());
        cancelLayout.setOnClickListener(new clickListener());


        if (filePath != null && filePath.length() > 0 && new File(filePath).exists()) {
            try {
                if (filePath.indexOf(".") > -1) {
                    tempType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
                }

                if (tempType.toLowerCase().equals("gif")) {
                    GifDrawable gifFromPath = new GifDrawable(filePath);
                    showImg.setImageDrawable(gifFromPath);
                } else {
                    showImg.setImageBitmap(showBitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            showImg.setImageBitmap(showBitmap);
        }

		/*if(bitStream != null && bitStream.size() >0){
            try {

				File gifFile = new File(Contants.BASE_IMAGE_DIR,"temp_tnzbsq.gif");

				if(!gifFile.exists()){
					gifFile.createNewFile();
				}

				FileOutputStream fileOut = new FileOutputStream(gifFile);
				bitStream.writeTo(fileOut);

				GifDrawable gifFromPath = new GifDrawable(gifFile);

				showImg.setImageDrawable(gifFromPath);
			}catch (Exception e){
				e.printStackTrace();
			}
		}*/

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.85); // 宽度设置为屏幕的0.85
        dialogWindow.setAttributes(lp);

        qqShareAction = new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).withMedia(image);

        //添加微信表情
        weixinShareAction = new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener);

        if (filePath != null) {
            UMEmoji umEmoji = new UMEmoji(getContext(), file);
            umEmoji.setThumb(imageThumb);
            weixinShareAction.withMedia(umEmoji);
        } else {
            image.setThumb(imageThumb);
            weixinShareAction.withMedia(image);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (image == null) {
                image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.def_logo));
            }

            int id = v.getId();
            switch (id) {
                case R.id.wechat_layout:

                    if (shareDialog == null) {
                        shareDialog = CustomProgress.create(context, "分享中...", true, null);
                    }
                    shareDialog.setCanceledOnTouchOutside(false);

                    if (weixinShareAction != null) {
                        weixinShareAction.share();
                    } else {
                        Toast.makeText(context, "分享失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.qq_layout:
                    if(tempType.equals("gif")){

                        if (shareDialog == null) {
                            shareDialog = CustomProgress.create(context, "正在分享...", true, null);
                        }
                        shareDialog.setCanceledOnTouchOutside(false);

                        if (isValidContext(context) && shareDialog != null) {
                            shareDialog.show();
                        }
                        saveImageToGallery(context, showBitmap);
                        if (fileName != null && file.exists()) {
                            shareGifToQQorQzone();
                        }
                    }else if (showBitmap != null) {
                        if (shareDialog == null) {
                            shareDialog = CustomProgress.create(context, "正在分享...", true, null);
                        }
                        shareDialog.setCanceledOnTouchOutside(false);

                        saveImageToGallery(context, showBitmap);
                        if (fileName != null && file.exists()) {
                            ShareImageToQQ(showBitmap,showBitmapThumb);
                        } else {
                            Toast.makeText(context, "分享失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } else {
                        Toast.makeText(context, "分享失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.local_layout:
                    saveImageToGallery(context, showBitmap);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    break;
                case R.id.cancel_layout:
                    closeShareDialog();
                    break;
                default:
                    break;
            }
        }
    }

    private void ShareImageToQQ(Bitmap image, Bitmap thumb) {
        UMImage pic = new UMImage((Activity) context, image);
        pic.setThumb(new UMImage((Activity) context, thumb));
        new ShareAction((Activity) context).withMedia(pic).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).share();
    }

    /**
     * 纯图片分享
     * 分享到QQ或者QQ空间
     */
    private void shareGifToQQorQzone() {
        //分享类型
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file.getAbsolutePath());

        App.mTencent.shareToQQ((Activity) context, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (isValidContext(context) && shareDialog != null) {
                    shareDialog.dismiss();
                    shareDialog = null;
                }
                try {
                    Toast.makeText(context, "QQ分享成功啦", Toast.LENGTH_SHORT).show();
                    closeShareDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (isValidContext(context) && shareDialog != null) {
                    shareDialog.dismiss();
                    shareDialog = null;
                }
                try {
                    Toast.makeText(context, "QQ分享失败", Toast.LENGTH_SHORT).show();
                    closeShareDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                if (isValidContext(context) && shareDialog != null) {
                    shareDialog.dismiss();
                    shareDialog = null;
                }
                try {
                    Toast.makeText(context, "QQ分享取消了", Toast.LENGTH_SHORT).show();
                    closeShareDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

   /* // 其次把文件插入到系统图库
    public void saveImageToGallery() {
        boolean isSave = true;
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

            MediaScannerConnection.scanFile(context, new String[]{filePath}, null, null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));

        } catch (FileNotFoundException e) {
            isSave = false;
            e.printStackTrace();
        }

        Message message = new Message();
        if(isSave){
            message.what = 0;
        }else{
            message.what = 1;
        }
        handler.sendMessage(message);
    }*/

    public void saveImageToGallery(Context context, Bitmap bmp) {

        File fileDir = new File(Contants.BASE_FIGHT_IMAGE_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        if (tempType.equals("gif")) {

            fileName = System.currentTimeMillis() + (int) (Math.random() * 10000) + "." + tempType;
            File newFile = new File(fileDir, fileName);
            if (file.exists()) {
                try {
                    newFile.createNewFile();
                    FileUtils.copyFile(file, newFile);
                    /*MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            newFile.getAbsolutePath(), fileName, null);*/
                    MediaScannerConnection.scanFile(context, new String[]{newFile.getAbsolutePath()}, null, null);
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + newFile.getAbsolutePath())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {

            if(fileName == null){
                fileName = System.currentTimeMillis() + (int) (Math.random() * 10000) + "." + tempType;
                file = new File(fileDir, fileName);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        bmp.compress(CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    }

                    //获取图片的路径
                    filePath = file.getAbsolutePath();

                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                    MediaScannerConnection.scanFile(context, new String[]{filePath}, null, null);
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
            if (isValidContext(context) && shareDialog != null) {
                shareDialog.dismiss();
                shareDialog = null;
            }
            try {
                Log.d("plat", "platform" + platform);
                Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
                closeShareDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (isValidContext(context) && shareDialog != null) {
                shareDialog.dismiss();
                shareDialog = null;
            }
            try {
                Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
                //closeShareDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (isValidContext(context) && shareDialog != null) {
                shareDialog.dismiss();
                shareDialog = null;
            }
            try {
                Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
                closeShareDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void showShareDialog(Dialog dia) {
        this.dialog = dia;
        if (isValidContext(context) && dialog != null) {
            this.dialog.show();
        }

        this.dialog.setOnDismissListener(new ShareDialogDismissListener());
    }

    //弹出窗口监听消失
    public class ShareDialogDismissListener implements OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            /*if (showBitmap != null) {
                showBitmap.recycle();
				showBitmap = null;
			}*/
            /*if (dialogBitmap != null) {
                dialogBitmap.recycle();
				dialogBitmap = null;
			}*/
        }
    }

    public void closeShareDialog() {
        if (isValidContext(context) && dialog != null && dialog.isShowing()) {
            dialog.dismiss();

			/*if(showBitmap != null){
				showBitmap.recycle();
				showBitmap = null;
			}*/
            if (image != null) {
                image = null;
            }
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
}