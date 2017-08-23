package com.fy.tnzbsq.bean;

import java.io.File;
import java.io.Serializable;

import com.fy.tnzbsq.util.CustomUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by zhangkai on 16/4/12.
 */
public class GameInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type = 0;
	public String url = "";
	public String name = "";
	public String imageUrl = "";
	public String description = "";
	public Bitmap iconBitmap;
	public String packageName;
	public void getIconBitmap() {
		File file = CustomUtils.downLoadFilePNG(imageUrl);
		if (file != null) {
			iconBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			try {
				iconBitmap = Bitmap.createScaledBitmap(iconBitmap, 120, 120, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
