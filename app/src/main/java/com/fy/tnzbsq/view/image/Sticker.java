package com.fy.tnzbsq.view.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


public class Sticker {

    private Bitmap bitmap;

    /**
     * 是否获取焦点
     */
    private boolean focusable;

    private Matrix mMatrix;

    /**
     * 边框画笔
     */
    private Paint mBorderPaint;

    private float[] mapPointsSrc;

    private float[] mapPointsDst = new float[10];

    private float scaleSize = 1.0f;

    public void setScaleSize(float scaleSize) {
        this.scaleSize = scaleSize;
    }

    public Sticker(Bitmap bitmap, int bgWidth, int bgHeight,int stickerHeight) {
        this.bitmap = bitmap;

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setFilterBitmap(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(5.0f);
        mBorderPaint.setColor(Color.RED);

        mMatrix = new Matrix();
        float initLeft = (bgWidth - bitmap.getWidth()) / 2;
        float initTop = stickerHeight - bitmap.getHeight()-50;
        mMatrix.postTranslate(initLeft, initTop);

        float px = bitmap.getWidth();
        float py = bitmap.getHeight();
        mapPointsSrc = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
    }

    public Sticker(Bitmap bitmap, int bgWidth, int bgHeight,int stickerHeight,String position) {
        this.bitmap = bitmap;

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setFilterBitmap(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(5.0f);
        mBorderPaint.setColor(Color.RED);

        mMatrix = new Matrix();
        float initLeft = (bgWidth - bitmap.getWidth()) / 2;
        float initTop;

        if(position != null){
            if(position.equals("1")){
                initTop = 0.88f * stickerHeight;
            }else{
                initTop = 0.1f * stickerHeight;
            }
        }else{
            initTop = 0.88f * stickerHeight;
        }

        mMatrix.postTranslate(initLeft, initTop);

        float px = bitmap.getWidth();
        float py = bitmap.getHeight();
        mapPointsSrc = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
    }

    public float[] getMapPointsDst() {
        return mapPointsDst;
    }

    public float getScaleSize() {
        return scaleSize;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public Matrix getmMatrix() {
        return mMatrix;
    }

    public Paint getmBorderPaint() {
        return mBorderPaint;
    }

    public float[] getMapPointsSrc() {
        return mapPointsSrc;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setMapPointsSrc(float[] mapPointsSrc) {
        this.mapPointsSrc = mapPointsSrc;

    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public void setmMatrix(Matrix mMatrix) {
        this.mMatrix = mMatrix;
    }

    public void setmBorderPaint(Paint mBorderPaint) {
        this.mBorderPaint = mBorderPaint;
    }
}
