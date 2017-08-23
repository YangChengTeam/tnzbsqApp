
package com.fy.tnzbsq.view.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.TextView;

public class GLFont {
	
	public static float FONT_HEIGHT = 0;
	
	public static float FONT_WIDTH = 0;

	/*
	 * 默认采用白色字体，宋体文字加粗
	 */
	public static Bitmap getImage(int width, int height, String mString, int size,int mainWidth,Typeface typeface,String colorRGB) {
		return getImage2(width, height, mString, size, colorRGB, typeface,mainWidth);
	}

	/*public static Bitmap getImage1(int width, int height, String mString, int size, int color) {
		return getImage2(width, height, mString, size, color, Typeface.create("宋体", Typeface.NORMAL),);
	}

	public static Bitmap getImage(int width, int height, String mString, int size, int color, String familyName) {
		return getImage2(width, height, mString, size, color, Typeface.create(familyName, Typeface.BOLD));
	}*/

	public static Bitmap getImage2(int width, int height, String mString, int size, String colorRGB, Typeface font,int mainWidth) {

		//第一次描边，内部边框
		TextPaint inpaint = new TextPaint();
		inpaint.setColor(Color.parseColor(colorRGB));
		inpaint.setTypeface(font);
		inpaint.setAntiAlias(true);// 去除锯齿
		inpaint.setFilterBitmap(true);// 对位图进行滤波处理
		inpaint.setTextSize(scalaFonts(size));
		inpaint.setDither(true);//防止抖动
		//第二次描边，外部边框
		TextPaint outpaint = new TextPaint();
		outpaint.setColor(Color.parseColor("#ffffff"));
		outpaint.setTypeface(font);
		outpaint.setAntiAlias(true);// 去除锯齿
		outpaint.setFilterBitmap(true);// 对位图进行滤波处理
		outpaint.setTextSize(scalaFonts(size));
		outpaint.setStrokeWidth(2);
		outpaint.setStyle(Paint.Style.STROKE);
		outpaint.setDither(true);//防止抖动

		FONT_WIDTH = getFontlength(outpaint, mString);
		FONT_HEIGHT = getFontHeight(outpaint);
		
		int x = (int) FONT_WIDTH + 60;
		int y = (int) (FONT_HEIGHT+60);
		int line = 0;
		
		if(FONT_WIDTH+100 > mainWidth){
			x = mainWidth - 200;
			line = (int) (FONT_WIDTH / x) + (FONT_WIDTH % x > 0 ? 1 : 0);
			y = (int) (FONT_HEIGHT * line + 60);
		}
		

		//float tX = (x - getFontlength(p, mString)) / 2;
		//float tY = (y - getFontHeight(p)) / 2 + getFontLeading(p);
		//canvasTemp.drawText(mString, tX, tY, p);
		
		StaticLayout layout = new StaticLayout(mString,inpaint,x,Alignment.ALIGN_NORMAL,1.0F,0.0F,false);
		StaticLayout layout1 = new StaticLayout(mString,outpaint,x,Alignment.ALIGN_NORMAL,1.0F,0.0F,false);
		Bitmap bmp = Bitmap.createBitmap(x, layout.getHeight(), Bitmap.Config.ARGB_8888);

		// 图象大小要根据文字大小算下,以和文本长度对应
		Canvas canvasTemp = new Canvas(bmp);
		canvasTemp.save();
		canvasTemp.translate(0,0);
		layout.draw(canvasTemp);
		layout1.draw(canvasTemp);
		canvasTemp.restore();
		return bmp;
	}

	/**
	 * 根据屏幕系数比例获取文字大小
	 *
	 * @return
	 */
	private static float scalaFonts(int size) {
		// 暂未实现
		return size;
	}

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

}
