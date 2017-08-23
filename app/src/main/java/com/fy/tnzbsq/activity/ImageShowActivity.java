package com.fy.tnzbsq.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.kymjs.kjframe.ui.BindView;

import com.fy.tnzbsq.R;
import com.polites.android.GestureImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

public class ImageShowActivity extends BaseActivity {

	@BindView(id = R.id.show_image)
	public GestureImageView showImg;
	private String imagePath = "";
	
	@Override
	public void setRootView() {
		setContentView(R.layout.image_show);
	}

	@Override
	public void initData() {
		super.initData();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		// 获取图片的下载路径
		if (bundle != null && bundle.getString("imagePath") != null && bundle.getString("imagePath").length() > 0) {
			imagePath = intent.getExtras().getString("imagePath");
		}
		
		if(imagePath != null && imagePath.length() > 0){
			Bitmap bit = getLoacalBitmap(imagePath);
			if(bit != null){
				showImg.setImageBitmap(bit);
			}else{
				showImg.setImageResource(R.mipmap.def_logo);
			}
		}

		showImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
}
