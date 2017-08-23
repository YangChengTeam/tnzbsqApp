package com.fy.tnzbsq.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class AlertUtil {
    public static void show(Context context,String msg){
	if(msg != null){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		// 可以控制toast显示的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
    }
    
    public static void show(Context context,int resId){
    	Toast toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
		// 可以控制toast显示的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }
}
