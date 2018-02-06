package com.fy.tnzbsq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.bumptech.glide.Glide;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.VideoUtils;
import com.fy.tnzbsq.view.FlikerProgressBar;
import com.fy.tnzbsq.view.GlideCircleTransform;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.other.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import rx.functions.Action1;

/**
 * Created by admin on 2018/1/29.
 */

public class VideoMergeBeforeActivity extends BaseAppActivity implements VideoUtils.mergeListener {

    public static String TAG = "VideoMergeBeforeActivity";

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.video_player)
    XinQuVideoPlayerStandard xinQuVideoPlayerStandard;

    //下载素材进度条
    @BindView(R.id.fliker_bar)
    FlikerProgressBar mFlikerProgressBar;

    @BindView(R.id.iv_merge_select)
    ImageView mMergeImageView;

    private String videoPlayPath;

    private String downUrl;

    private String videoName;

    private String downSavePath;

    private String downSaveFilePath;

    private String videoId;

    private ProgressDialog createDialog;

    List<String> mSelectedImages;

    private int cWidth;

    private int cHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_before;
    }

    @Override
    protected void initVars() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            videoId = bundle.getString("video_id", "");
        }

        //视频预览的地址
        videoPlayPath = "http://nz.qqtn.com/zbsq/Apk/n1.mp4";

        //素材文件下载地址
        downUrl = "http://nz.qqtn.com/zbsq/Apk/happy_video2.zip";

        //下载ZIP保存的目录
        downSavePath = Contants.BASE_NORMAL_FILE_DIR + "/" + videoId;
        FileUtils.createOrExistsDir(downSavePath);

        //下载的ZIP文件路径
        downSaveFilePath = downSavePath + "/" + videoId + ".zip";

        //视频
        videoName = videoId + File.separator + "mp4";

        mToolbar.setTitle("视频制作");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFlikerProgressBar.setProgress(0);
        File saveFile = new File(downSaveFilePath);
        if (saveFile.exists()) {
            mFlikerProgressBar.setProgress(100);
            mFlikerProgressBar.finishLoad();
        }

        FileDownloader.setup(this);

        createDialog = new ProgressDialog(this);
        createDialog.setTitle("合成视频");
        createDialog.setMessage("正在合成中");
        RxView.clicks(mFlikerProgressBar).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mFlikerProgressBar.isFinish()) {
                    createDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String unZipImagePath = downSavePath + "/happy_video/wait_images";
                            if (FileUtils.createOrExistsDir(unZipImagePath)) {
                                mergePositionVideo(unZipImagePath);
                            }
                        }
                    }).start();
                } else {
                    mFlikerProgressBar.setFinish(false);
                    mFlikerProgressBar.setStop(false);
                    mFlikerProgressBar.setProgress(0);
                    downVideo();
                }
                //mergeImage();
            }
        });

        cWidth = 300;
        cHeight = 300;

        //图片选择
        RxView.clicks(mMergeImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(VideoMergeBeforeActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mSelectedImages = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (mSelectedImages != null && mSelectedImages.size() > 0) {

                    HeadImageUtils.imgPath = mSelectedImages.get(0);

                    Intent intent = new Intent(VideoMergeBeforeActivity.this, ImageCropActivity.class);
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
            Glide.with(this).load(tempFile).transform(new GlideCircleTransform(context)).into(mMergeImageView);
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String coverUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517219975491&di=2eb4289e328cdc0c084ce7605ceb026f&imgtype=0&src=http%3A%2F%2F5.1015600.com%2F2014%2Fpic%2F000%2F354%2F42a8cd8f271b46a9e67981d5c1ad4b88.jpg";
        Glide.with(this).load(coverUrl).override(SizeUtils.dp2px(340), SizeUtils.dp2px(400)).into(xinQuVideoPlayerStandard.thumbImageView);
        xinQuVideoPlayerStandard.setUp(videoPlayPath, XinQuVideoPlayer.SCREEN_WINDOW_NORMAL, false, "");
    }

    /**
     * 下载
     */
    public void downVideo() {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(downUrl)
                .setPath(downSaveFilePath, false)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        final double progress = (double) soFarBytes / (double) totalBytes * 100;
                        VideoMergeBeforeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFlikerProgressBar.setProgress((int) progress);
                            }
                        });
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    LogUtils.i(TAG, "unzip--->");
                                    ZipUtils.unzipFile(downSaveFilePath, downSavePath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        VideoMergeBeforeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFlikerProgressBar.setProgress(100);
                                mFlikerProgressBar.finishLoad();
                            }
                        });
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });
        baseDownloadTask.setAutoRetryTimes(10);
        baseDownloadTask.start();
    }

    /**
     * 合成指定位置的视频
     */
    public void mergePositionVideo(String basePath) {
        int width = 0;
        int height = 0;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        File file = new File(basePath + "/image1.jpg");
        if (file.exists()) {
            Bitmap tempBitmap = ImageUtils.getBitmap(file);
            width = tempBitmap.getWidth();
            height = tempBitmap.getHeight();
        }

        String headPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/00ff/head_icon.png";
        VideoUtils.getInstance(this).setMergeListener(this);
        VideoUtils.getInstance(this).mergeImagesToMp4(basePath, width, height, headPath, 375, 375, 72, 506, 29, 109, 10);
    }


    /**
     * 像素替换的方法
     */
    public void mergeImage() {
        LogUtils.i("mergeImage start --->" + TimeUtils.getNowString());
        String basePath = Environment.getExternalStorageDirectory().getPath();

        String baseImg = basePath + "/000ff/fengjing1.png";

        String picImg = basePath + "/000ff/pip_test1.png";

        String maskImg = basePath + "/000ff/pip_2_frame_mask.png";

        Bitmap baseBitmap = ImageUtils.getBitmap(baseImg);
        Bitmap picBitmap = ImageUtils.getBitmap(picImg);
        Bitmap maskBitmap = ImageUtils.getBitmap(maskImg);

        Bitmap compBitmap = VideoUtils.getInstance(this).getFrontBitMap(maskBitmap, picBitmap);

        Bitmap resultBitMap = ImageUtils.addImageWatermark(compBitmap, baseBitmap, 0, 0, 255, false);
        for (int i = 0; i < 100; i++) {
            ImageUtils.save(resultBitMap, new File(basePath + "/000ff/result_image" + i + ".jpg"), Bitmap.CompressFormat.JPEG);
        }

        LogUtils.i("mergeImage end --->" + TimeUtils.getNowString());
    }

    @Override
    public void mergeSuccess() {
        if (createDialog != null && createDialog.isShowing()) {
            ToastUtil.toast(this, "合并完成");
            createDialog.dismiss();
        }

        VideoMergeBeforeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String videoPath = downSavePath + "/happy_video/wait_images/result.mp4";
                xinQuVideoPlayerStandard.setUp(videoPath, XinQuVideoPlayer.SCREEN_WINDOW_NORMAL, false, "");
            }
        });

    }

    @Override
    public void mergeFail() {
        if (createDialog != null && createDialog.isShowing()) {
            createDialog.dismiss();
        }
    }
}
