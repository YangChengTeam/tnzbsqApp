package com.fy.tnzbsq.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

/**
 * Created by admin on 2018/1/30.
 */

public class VideoUtils {

    private Context mContext;

    //private FFmpeg ffmpeg;

    private static VideoUtils instance;

    public interface mergeListener {
        void mergeSuccess();

        void mergeFail();
    }

    private mergeListener mergeListener;

    public void setMergeListener(VideoUtils.mergeListener mergeListener) {
        this.mergeListener = mergeListener;
    }

    private VideoUtils(Context context) {
        this.mContext = context;
        loadFFmpeg();
    }

    public static VideoUtils getInstance(Context context) {
        if (instance == null) {
            instance = new VideoUtils(context);
        }
        return instance;
    }

    private void loadFFmpeg() {
        /*ffmpeg = FFmpeg.getInstance(mContext);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onFinish() {
                    Logger.e("loadFFmpeg --- finish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
        }*/
    }

    /**
     * 将视频文件分解为图片序列
     *
     * @param videoPath
     * @param outImagePath
     */
    public void mp4ToImages(String videoPath, String outImagePath) {
        /*final String[] cmds = new String[]{"-i", videoPath, "-q:v", "2", outImagePath};
        try {
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Logger.e("success --->" + message);
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    LogUtils.i("mp4ToImages cmd --->" + cmds.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * @param mergeBasePath 图片所在目录
     * @param baseWidth     合成的底图宽度
     * @param baseHeight    合成的底图高度
     * @param headPath      头像的路径
     * @param headWidth     头像的宽度
     * @param headHeight    头像的高度
     * @param startX        与底图合成时，头像的起始位置
     * @param startY
     * @param mergeStart    图片目录合成的开始张数
     * @param totalCount    总的图片数
     */
    public void mergeImagesToMp4(String mergeBasePath, int baseWidth, int baseHeight, String headPath, int headWidth, int headHeight, int startX, int startY, int mergeStart, int totalCount, int fps) {

        LogUtils.i("start time --->" + com.blankj.utilcode.util.TimeUtils.getNowString());

        //1.创建合成前底图
        Bitmap baseBitmap = Bitmap.createBitmap(baseWidth, baseHeight, Bitmap.Config.RGB_565);
        //2.创建头像
        Bitmap headBitMap = ImageUtils.getBitmap(new File(headPath), headWidth, headHeight);
        //3.合成底图与头像
        Bitmap baseResultBitmap = ImageUtils.addImageWatermark(baseBitmap, headBitMap, startX, startY, 255);

        //将合成的底图与视频中每一帧图片合成
        for (int i = 1; i <= totalCount; i++) {

            LogUtils.i("merge image 111---" + i + " --->" + com.blankj.utilcode.util.TimeUtils.getNowString());

            if (i < mergeStart) {
                String imagePath = mergeBasePath + "/image" + i + ".jpg";
                com.blankj.utilcode.util.FileUtils.rename(imagePath, "result_image" + i + ".jpg");
            } else {
                String imagePath = mergeBasePath + "/image" + i + ".png";
                Bitmap iBitMap = ImageUtils.getBitmap(new File(imagePath));
                Bitmap resultBitMap = addImageWatermark(baseResultBitmap, iBitMap, 0, 0, 255, false);
                ImageUtils.save(resultBitMap, new File(mergeBasePath + "/result_image" + i + ".jpg"), Bitmap.CompressFormat.JPEG);
            }

            LogUtils.i("merge image 222---" + i + " --->" + com.blankj.utilcode.util.TimeUtils.getNowString());
        }

        LogUtils.i("merge image end time --->" + com.blankj.utilcode.util.TimeUtils.getNowString());

        //生成前先删除已存在的视频
        File tempFile = new File(mergeBasePath + "/result.mp4");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        //合成视频
        String cmd = "-r " + fps + " -i " + mergeBasePath + "/result_image%d.jpg" + " -vcodec mpeg4 " + mergeBasePath + "/result.mp4";
        LogUtils.i("merge video cmds" + cmd);
        EpEditor.execCmd(cmd, 0, new OnEditorListener() {
            @Override
            public void onSuccess() {
                LogUtils.i("merge video end time --->" + com.blankj.utilcode.util.TimeUtils.getNowString());
                LogUtils.i("mergeImagesToMp4 success --->");
                mergeListener.mergeSuccess();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    public static Bitmap addImageWatermark(final Bitmap src,
                                           final Bitmap watermark,
                                           final int x,
                                           final int y,
                                           final int alpha,
                                           final boolean recycle) {
        if (src == null) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        if (watermark != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            Rect srect = new Rect(0, 0, watermark.getWidth(), watermark.getHeight());
            Rect drect = new Rect(0, 0, watermark.getWidth(), watermark.getHeight());
            canvas.drawBitmap(watermark, srect, drect, paint);
        }
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * 获得前置图片
     *
     * @return
     */
    public Bitmap getFrontBitMap(Bitmap maskBitmap, Bitmap picBitmap) {

        int width = maskBitmap.getWidth();
        int height = maskBitmap.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //前置相片添加蒙板效果
        int[] picPixels = new int[width * height];
        int[] maskPixels = new int[width * height];

        maskBitmap.getPixels(maskPixels, 0, width, 0, 0, width, height);
        picBitmap.getPixels(picPixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < maskPixels.length; i++) {
            if (maskPixels[i] == 0xff000000) {
                picPixels[i] = 0;
            } else if (maskPixels[i] == 0) {
                //donothing
            } else {
                //把mask的a通道应用与picBitmap
                maskPixels[i] &= 0xff000000;
                maskPixels[i] = 0xff000000 - maskPixels[i];
                picPixels[i] &= 0x00ffffff;
                picPixels[i] |= maskPixels[i];
            }
        }

        //生成前置图片添加蒙板后的bitmap:resultBitmap
        resultBitmap.setPixels(picPixels, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

}
