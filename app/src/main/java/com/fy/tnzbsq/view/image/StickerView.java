package com.fy.tnzbsq.view.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fy.tnzbsq.R;

import org.kymjs.kjframe.utils.DensityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StickerView extends View {

    /**
     * 最大放大倍数
     */
    public static final float MAX_SCALE_SIZE = 5.0f;
    public static final float MIN_SCALE_SIZE = 0.5f;

    private PointF mContentDstLeftTopPoint = new PointF();
    private PointF mContentDstRightTopPoint = new PointF();
    private PointF mContentDstLeftBottomPoint = new PointF();
    private PointF mContentDstRigintBottomPoint = new PointF();

    private RectF mViewRect;

    private float mLastPointX, mLastPointY, deviation;

    private Bitmap mControllerBitmap, mDeleteBitmap, bgBitmap;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private boolean mInController, mInMove;

    private boolean mInDelete = false;

    // private Sticker currentSticker;
    private List<Sticker> stickers = new ArrayList<Sticker>();

    /**
     * 焦点贴纸索引
     */
    private int focusStickerPosition = -1;

    private String canvasText;

    private Matrix lastTextMatrix;//最后一次文字的位置

    private Matrix lastBubbleMatrix;//最后一次气泡的位置

    private boolean isMove = false;

    private PaintFlagsDrawFilter pfd;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sticker_control);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

    }

    public void setWaterMark(Bitmap bitmap, Bitmap bgBitmap, int stickerHeight) {
        this.bgBitmap = bgBitmap;
        Point point = Utils.getDisplayWidthPixels(getContext());

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
        }

        Sticker sticker = new Sticker(bitmap, point.x, point.x, stickerHeight);
        stickers.add(sticker);
        focusStickerPosition = stickers.size() - 1;
        setFocusSticker(focusStickerPosition);
        postInvalidate();
    }

    public void setWaterMark(Bitmap bitmap, Bitmap bgBitmap, int stickerHeight, String position) {
        this.bgBitmap = bgBitmap;
        Point point = Utils.getDisplayWidthPixels(getContext());

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
        }

        Sticker sticker = new Sticker(bitmap, point.x, point.x, stickerHeight, position);
        stickers.add(sticker);
        focusStickerPosition = stickers.size() - 1;
        setFocusSticker(focusStickerPosition);
        postInvalidate();
    }


    //Sticker sticker;

    public void setWaterMark(Bitmap bitmap, Bitmap bgBitmap, int stickerHeight, String position, String canvasText) {
        this.bgBitmap = bgBitmap;
        Point point = Utils.getDisplayWidthPixels(getContext());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
        }

        this.canvasText = canvasText;
        Sticker sticker = new Sticker(bitmap, point.x, point.x, stickerHeight, position);
        stickers.add(sticker);

        focusStickerPosition = stickers.size() - 1;
        setFocusSticker(focusStickerPosition);
        //stickers.get(focusStickerPosition).setFocusable(true);
        postInvalidate();
    }

    //文字改变
    public void setWaterMarkImageDiy(Bitmap bitmap, Bitmap bgBitmap, int stickerHeight, String canvasText) {
        this.bgBitmap = bgBitmap;
        Point point = Utils.getDisplayWidthPixels(getContext());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
        }

        this.canvasText = canvasText;

        Sticker sticker = new Sticker(bitmap, point.x, point.x, stickerHeight);
        stickers.add(sticker);
        focusStickerPosition = stickers.size() - 1;
        setFocusSticker(focusStickerPosition);
        postInvalidate();
    }

    //气泡改变
    public void setWaterMarkImageBullbe(Bitmap bitmap, Bitmap textBitmap, Bitmap bgBitmap, int stickerHeight, String canvasText) {
        this.bgBitmap = bgBitmap;
        Point point = Utils.getDisplayWidthPixels(getContext());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
        }

        this.canvasText = canvasText;

        Sticker sticker = new Sticker(bitmap, point.x, point.x, stickerHeight);
        stickers.add(sticker);
        if(textBitmap != null) {
            Sticker stickerText = new Sticker(textBitmap, point.x, point.x, stickerHeight);
            stickers.add(stickerText);
        }
        focusStickerPosition = stickers.size() - 1;
        setFocusSticker(focusStickerPosition);
        postInvalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("onDraw----", "onDraw----");
        if (bgBitmap == null) {
            bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
        }

        int leftW = (DensityUtils.getScreenW(getContext()) - bgBitmap.getWidth()) / 2;
        canvas.drawBitmap(bgBitmap, leftW, 0, null);

        if (stickers.size() <= 0) {
            return;
        }

        //if (canvasText != null && canvasText.length() > 0) {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.line_color));


            if (stickers != null && stickers.size() == 1) {
                int i = 0;
                if (lastTextMatrix != null) {
                    stickers.get(i).setmMatrix(lastTextMatrix);
                }

                //赋值最新的坐标点
                stickers.get(i).getmMatrix().mapPoints(stickers.get(i).getMapPointsDst(),
                        stickers.get(i).getMapPointsSrc());

                canvas.drawBitmap(stickers.get(i).getBitmap(), stickers.get(i).getmMatrix(), null);
                if (stickers.get(i).isFocusable()) {
                    canvas.drawLine(stickers.get(i).getMapPointsDst()[0], stickers.get(i).getMapPointsDst()[1],
                            stickers.get(i).getMapPointsDst()[2], stickers.get(i).getMapPointsDst()[3],
                            stickers.get(i).getmBorderPaint());
                    canvas.drawLine(stickers.get(i).getMapPointsDst()[2], stickers.get(i).getMapPointsDst()[3],
                            stickers.get(i).getMapPointsDst()[4], stickers.get(i).getMapPointsDst()[5],
                            stickers.get(i).getmBorderPaint());
                    canvas.drawLine(stickers.get(i).getMapPointsDst()[4], stickers.get(i).getMapPointsDst()[5],
                            stickers.get(i).getMapPointsDst()[6], stickers.get(i).getMapPointsDst()[7],
                            stickers.get(i).getmBorderPaint());
                    canvas.drawLine(stickers.get(i).getMapPointsDst()[6], stickers.get(i).getMapPointsDst()[7],
                            stickers.get(i).getMapPointsDst()[0], stickers.get(i).getMapPointsDst()[1],
                            stickers.get(i).getmBorderPaint());

                    canvas.drawBitmap(mControllerBitmap, stickers.get(i).getMapPointsDst()[4] - mControllerWidth / 2,
                            stickers.get(i).getMapPointsDst()[5] - mControllerHeight / 2, null);
                }
                if(isMove){
                    lastTextMatrix = stickers.get(i).getmMatrix();
                }else{
                    lastTextMatrix = null;
                }

            }

            if (stickers != null && stickers.size() == 2) {
                for (int i = 0; i < stickers.size(); i++) {
                    //如果是改成文字内容信息时，才设置当前文字的位置为最后一次记录的位置信息

                    if (i == 0) {
                        if (lastBubbleMatrix != null) {
                            stickers.get(i).setmMatrix(lastBubbleMatrix);
                        }
                    }
                    if (i == 1) {
                        if (lastTextMatrix != null) {
                            stickers.get(i).setmMatrix(lastTextMatrix);
                        }
                    }

                    //赋值最新的坐标点
                    stickers.get(i).getmMatrix().mapPoints(stickers.get(i).getMapPointsDst(),
                            stickers.get(i).getMapPointsSrc());

                    canvas.drawBitmap(stickers.get(i).getBitmap(), stickers.get(i).getmMatrix(), null);
                    if (stickers.get(i).isFocusable()) {
                        canvas.drawLine(stickers.get(i).getMapPointsDst()[0], stickers.get(i).getMapPointsDst()[1],
                                stickers.get(i).getMapPointsDst()[2], stickers.get(i).getMapPointsDst()[3],
                                stickers.get(i).getmBorderPaint());
                        canvas.drawLine(stickers.get(i).getMapPointsDst()[2], stickers.get(i).getMapPointsDst()[3],
                                stickers.get(i).getMapPointsDst()[4], stickers.get(i).getMapPointsDst()[5],
                                stickers.get(i).getmBorderPaint());
                        canvas.drawLine(stickers.get(i).getMapPointsDst()[4], stickers.get(i).getMapPointsDst()[5],
                                stickers.get(i).getMapPointsDst()[6], stickers.get(i).getMapPointsDst()[7],
                                stickers.get(i).getmBorderPaint());
                        canvas.drawLine(stickers.get(i).getMapPointsDst()[6], stickers.get(i).getMapPointsDst()[7],
                                stickers.get(i).getMapPointsDst()[0], stickers.get(i).getMapPointsDst()[1],
                                stickers.get(i).getmBorderPaint());

                        canvas.drawBitmap(mControllerBitmap, stickers.get(i).getMapPointsDst()[4] - mControllerWidth / 2,
                                stickers.get(i).getMapPointsDst()[5] - mControllerHeight / 2, null);
                    }

                    //记录最后一次的“文字图片”的位置信息
                    if (i == 0) {
                        lastBubbleMatrix = stickers.get(i).getmMatrix();
                    }

                    if (i == 1) {
                        lastTextMatrix = stickers.get(i).getmMatrix();
                    }
                }
            }
        //}

    }

    /**
     * 是否在控制点区域
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isInController(float x, float y) {
        int position = 4;
        float rx = stickers.get(focusStickerPosition).getMapPointsDst()[position];
        float ry = stickers.get(focusStickerPosition).getMapPointsDst()[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2, ry - mControllerHeight / 2, rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        return false;

    }

    /**
     * 是否在删除点区域
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isInDelete(float x, float y) {
        int position = 0;
        float rx = stickers.get(focusStickerPosition).getMapPointsDst()[position];
        float ry = stickers.get(focusStickerPosition).getMapPointsDst()[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2, ry - mDeleteHeight / 2, rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        return false;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }

        if (stickers.size() <= 0 || focusStickerPosition < 0) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;

                    float nowLenght = caculateLength(stickers.get(focusStickerPosition).getMapPointsDst()[0],
                            stickers.get(focusStickerPosition).getMapPointsDst()[1]);
                    float touchLenght = caculateLength(x, y);
                    deviation = touchLenght - nowLenght;
                    break;
                }

                if (isInDelete(x, y)) {
                    mInDelete = true;
                    break;
                }

                if (isFocusSticker(x, y)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                    invalidate();
                } else {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                }
            case MotionEvent.ACTION_CANCEL:
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInController) {
                    stickers.get(focusStickerPosition).getmMatrix().postRotate(rotation(event),
                            stickers.get(focusStickerPosition).getMapPointsDst()[8],
                            stickers.get(focusStickerPosition).getMapPointsDst()[9]);
                    float nowLenght = caculateLength(stickers.get(focusStickerPosition).getMapPointsDst()[0],
                            stickers.get(focusStickerPosition).getMapPointsDst()[1]);
                    float touchLenght = caculateLength(x, y) - deviation;
                    if (Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = stickers.get(focusStickerPosition).getScaleSize() * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            stickers.get(focusStickerPosition).getmMatrix().postScale(scale, scale,
                                    stickers.get(focusStickerPosition).getMapPointsDst()[8],
                                    stickers.get(focusStickerPosition).getMapPointsDst()[9]);
                            stickers.get(focusStickerPosition).setScaleSize(nowsc);
                        }
                    }

                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;
                }

                if (mInMove == true) {
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;

                    if (Math.sqrt(cX * cX + cY * cY) > 2.0f && canStickerMove(cX, cY)) {
                        stickers.get(focusStickerPosition).getmMatrix().postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }
        }
        if(mInMove){
            isMove = true;
        }

        if (mInController || mInMove) {
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 删除所有贴纸
     */
    private void doDeleteSticker() {
        // 暂时注释，不删除贴纸
        // stickers.remove(focusStickerPosition);
        // focusStickerPosition = stickers.size() - 1;
        // invalidate();
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + stickers.get(focusStickerPosition).getMapPointsDst()[8];
        float py = cy + stickers.get(focusStickerPosition).getMapPointsDst()[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }

    private float caculateLength(float x, float y) {
        return (float) Utils.lineSpace(x, y, stickers.get(focusStickerPosition).getMapPointsDst()[8],
                stickers.get(focusStickerPosition).getMapPointsDst()[9]);
    }

    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - stickers.get(focusStickerPosition).getMapPointsDst()[8];
        double delta_y = y - stickers.get(focusStickerPosition).getMapPointsDst()[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * 是否点击在贴纸区域
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isFocusSticker(float x, float y) {
        for (int i = stickers.size() - 1; i >= 0; i--) {
            Sticker sticker = stickers.get(i);
            if (isInContent(x, y, sticker)) {
                setFocusSticker(i);
                return true;
            }
        }
        setFocusSticker(-1);
        return false;
    }

    /**
     * 判断点是否在指定区域内
     *
     * @param 'x'
     * @param 'y'
     * @return
     */
    private boolean isInContent(float touchX, float touchY, Sticker currentSticker) {
        // long startTime = System.currentTimeMillis();
        // float[] pointsDst = currentSticker.getMapPointsDst();
        // PointD pointF_1 = Utils.getMidpointCoordinate(pointsDst[0],
        // pointsDst[1], pointsDst[2], pointsDst[3]);
        // double a1 = Utils.lineSpace(pointsDst[8], pointsDst[9],
        // pointF_1.getX(), pointF_1.getY());
        // double b1 = Utils.lineSpace(pointsDst[8], pointsDst[9], x, y);
        // if (b1 <= a1) {
        // return true;
        // }
        // double c1 = Utils.lineSpace(pointF_1.getX(), pointF_1.getY(), x, y);
        // double p1 = (a1 + b1 + c1) / 2;
        // double s1 = Math.sqrt(p1 * (p1 - a1) * (p1 - b1) * (p1 - c1));
        // double d1 = 2 * s1 / a1;
        // if (d1 > a1) {
        // return false;
        // }

        // PointD pointF_2 = Utils.getMidpointCoordinate(pointsDst[2],
        // pointsDst[3], pointsDst[4], pointsDst[5]);
        // double a2 = a1;
        // double b2 = b1;
        // double c2 = Utils.lineSpace(pointF_2.getX(), pointF_2.getY(), x, y);
        // double p2 = (a2 + b2 + c2) / 2;
        // double temp = p2 * (p2 - a2) * (p2 - b2) * (p2 - c2);
        // double s2 = Math.sqrt(temp);
        // double d2 = 2 * s2 / a2;
        // if (d2 > a1) {
        // return false;
        // }
        // long endTime = System.currentTimeMillis();
        // long time = endTime - startTime;

        // if (d1 <= a1 && d2 <= a1) {
        // return true;
        // }

        // return false;
        float[] f = new float[9];
        currentSticker.getmMatrix().getValues(f);
        mContentDstLeftTopPoint.x = f[0] * 0 + f[1] * 0 + f[2];
        mContentDstLeftTopPoint.y = f[3] * 0 + f[4] * 0 + f[5];
        mContentDstRightTopPoint.x = f[0] * currentSticker.getBitmap().getWidth() + f[1] * 0 + f[2];
        mContentDstRightTopPoint.y = f[3] * currentSticker.getBitmap().getWidth() + f[4] * 0 + f[5];
        mContentDstLeftBottomPoint.x = f[0] * 0 + f[1] * currentSticker.getBitmap().getHeight() + f[2];
        mContentDstLeftBottomPoint.y = f[3] * 0 + f[4] * currentSticker.getBitmap().getHeight() + f[5];
        mContentDstRigintBottomPoint.x = f[0] * currentSticker.getBitmap().getWidth()
                + f[1] * currentSticker.getBitmap().getHeight() + f[2];
        mContentDstRigintBottomPoint.y = f[3] * currentSticker.getBitmap().getWidth()
                + f[4] * currentSticker.getBitmap().getHeight() + f[5];

        PointF pointF = new PointF(touchX, touchY);
        PointF[] vertexPointFs = new PointF[]{mContentDstLeftTopPoint, mContentDstRightTopPoint,
                mContentDstRigintBottomPoint, mContentDstLeftBottomPoint};
        int nCross = 0;
        for (int i = 0; i < vertexPointFs.length; i++) {
            PointF p1 = vertexPointFs[i];
            PointF p2 = vertexPointFs[(i + 1) % vertexPointFs.length];
            if (p1.y == p2.y)
                continue;
            if (pointF.y < Math.min(p1.y, p2.y))
                continue;
            if (pointF.y >= Math.max(p1.y, p2.y))
                continue;
            double x = (double) (pointF.y - p1.y) * (double) (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;
            if (x > pointF.x)
                nCross++;
        }
        return (nCross % 2 == 1);
    }



    /**
     * 返回合并后的图片
     *
     * @return
     */
    public List<Bitmap> getGifBitmaps(List<Bitmap> bitmaps) {

        List<Bitmap> resultBitmaps = new ArrayList<>();
        try {
            pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
            Matrix matrix1 = new Matrix();
            for (int i = 0; i < bitmaps.size(); i++) {
                Bitmap bitmap = bitmaps.get(i);

                if (i == 0) {
                    float scaleX = (float) bitmap.getWidth() / (float) DensityUtils.getScreenW(getContext());

                    float tempPro = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                    float newHeight = tempPro * DensityUtils.getScreenW(getContext());

                    float scaleY = (float) bitmap.getHeight() / newHeight;

                    matrix1.postConcat(stickers.get(focusStickerPosition).getmMatrix());
                    matrix1.postScale(scaleX, scaleY);
                }

                Bitmap cvBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas cv = new Canvas(cvBitmap);
                cv.setDrawFilter(pfd);
                cv.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                cv.drawColor(Color.parseColor("#ffffff"));

                int leftW = (DensityUtils.getScreenW(getContext()) - bitmap.getWidth()) / 2;
                cv.drawBitmap(bitmap, 0, 0, null);
                cv.drawBitmap(stickers.get(focusStickerPosition).getBitmap(), matrix1, null);

                cv.save(Canvas.ALL_SAVE_FLAG);
                cv.restore();
                resultBitmaps.add(cvBitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBitmaps;
    }


    /**
     * 返回合并后的图片
     *
     * @return
     */
    public Bitmap saveFightBitmapToFile(int newHeight) {

        Bitmap newbmp = null;
        try {
            // 宽度为屏幕宽度
            int bgWidth = DensityUtils.getScreenW(getContext());
            int bgHeight = newHeight;
            newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newbmp);
            cv.drawColor(Color.parseColor("#ffffff"));

            int leftW = (DensityUtils.getScreenW(getContext()) - bgBitmap.getWidth()) / 2;
            cv.drawBitmap(bgBitmap, leftW, 0, null);
            cv.drawBitmap(stickers.get(focusStickerPosition).getBitmap(),
                    stickers.get(focusStickerPosition).getmMatrix(), null);
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newbmp;
    }

    /**
     * 返回合并后的图片
     *
     * @return
     */
    public Bitmap saveBitmapToFile() {

        Bitmap newbmp = null;
        try {
            // 宽度为屏幕宽度
            int bgWidth = DensityUtils.getScreenW(getContext());
            int bgHeight = DensityUtils.getScreenH(getContext()) - DensityUtils.dip2px(getContext(), 285);
            newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newbmp);
            cv.drawColor(Color.parseColor("#ffffff"));

            int leftW = (DensityUtils.getScreenW(getContext()) - bgBitmap.getWidth()) / 2;
            cv.drawBitmap(bgBitmap, leftW, 0, null);
            cv.drawBitmap(stickers.get(focusStickerPosition).getBitmap(),
                    stickers.get(focusStickerPosition).getmMatrix(), null);
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newbmp;
    }

    /**
     * 返回合并包含有气泡的图片
     *
     * @return
     */
    public Bitmap saveBubbleBitmapToFile() {

        Bitmap newbmp = null;
        try {
            int bgWidth = DensityUtils.getScreenW(getContext());
            int bgHeight = DensityUtils.getScreenH(getContext()) - DensityUtils.dip2px(getContext(), 285);
            newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newbmp);
            cv.drawColor(Color.parseColor("#ffffff"));

            int leftW = (DensityUtils.getScreenW(getContext()) - bgBitmap.getWidth()) / 2;
            cv.drawBitmap(bgBitmap, leftW, 0, null);

            for (int i = 0; i < stickers.size(); i++) {
                //if (i != focusStickerPosition) {
                    cv.drawBitmap(stickers.get(i).getBitmap(), stickers.get(i).getmMatrix(), null);
                //}
            }

            /*cv.drawBitmap(stickers.get(focusStickerPosition).getBitmap(),
                    stickers.get(focusStickerPosition).getmMatrix(), null);*/

            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newbmp;
    }

    /**
     * 保存图片到图库
     *
     * @param bmp
     */
    public void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "0000Test");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // // 其次把文件插入到系统图库
        // try {
        // MediaStore.Images.Media.insertImage(context.getContentResolver(),
        // file.getAbsolutePath(), fileName, null);
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }
        // // 最后通知图库更新
        // context.sendBroadcast(new
        // Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" +
        // file.getAbsolutePath())));
    }

    public void clearBitmap() {
        stickers.clear();
        focusStickerPosition = -1;
        invalidate();
    }

    /**
     * 设置焦点贴纸
     *
     * @param position
     */
    private void setFocusSticker(int position) {
        int focusPosition = stickers.size() - 1;
        for (int i = 0; i < stickers.size(); i++) {
            if (i == position) {
                focusPosition = i;
                stickers.get(i).setFocusable(true);
            } else {
                stickers.get(i).setFocusable(false);
            }
        }
        Sticker sticker = stickers.remove(focusPosition);
        stickers.add(focusPosition,sticker);
        focusStickerPosition = focusPosition;
    }
}
