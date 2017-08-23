package com.fy.tnzbsq.view;

import com.fy.tnzbsq.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class PicSelectDialog {
	
	public static Dialog showPhotoDialog(Context context,String title,
			int[] imagesId,String[] items,final PicDialogItemOnClick listener)
	{
		final Dialog dialog=new Dialog(context, R.style.DialogStyle);
		
		View view=LayoutInflater.from(context).inflate(R.layout.pic_select_dialog, null);
		
		dialog.setContentView(view);
		
		dialog.setCanceledOnTouchOutside(true);
		
		((TextView)view.findViewById(R.id.dialog_title)).setText(title);
		
		
		ImageView local_shape=(ImageView)view.findViewById(R.id.set_image);
		ImageView camera_shape=(ImageView)view.findViewById(R.id.set_image11);
		
		final TextView local_picture=(TextView)view.findViewById(R.id.me_picture);
		final TextView local_camera=(TextView)view.findViewById(R.id.me_camera_picture);
		
		local_shape.setImageResource(imagesId[0]);
		camera_shape.setImageResource(imagesId[1]);
		
		local_picture.setText(items[0]);
		local_camera.setText(items[1]);
		
		
		
		local_picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				listener.itemSelect(local_picture.getText().toString());
			}
		});
		
		local_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				listener.itemSelect(local_camera.getText().toString());
			}
		});
		
		
		Window mWindow=dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width= getScreenWidth(context);
		mWindow.setGravity(Gravity.CENTER);
		mWindow.setAttributes(lp);
		dialog.show();
		
		return dialog;
		
	}
	
	public interface PicDialogItemOnClick{
		public abstract void itemSelect(String itemSelect);
	}
	
	public static int getScreenWidth(Context context)
	{
		DisplayMetrics me=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(me);
		return me.widthPixels;
	}

}
