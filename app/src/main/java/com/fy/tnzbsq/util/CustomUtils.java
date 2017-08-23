package com.fy.tnzbsq.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by zhangkai on 16/4/7.
 */
public class CustomUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean hasActiveInternetConnection(Context context,String url) {
        if (hasActiveInternetConnection(context, url)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(url).openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {

            }
        } else {

        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static int getHeight(Context paramContext)
    {
        if (Build.VERSION.SDK_INT >= 13)
        {
            Display localDisplay = ((WindowManager) paramContext.getSystemService(Application.WINDOW_SERVICE)).getDefaultDisplay();
            Point localPoint = new Point();
            //localDisplay.getSize(localPoint);
            return localPoint.y;
        }
        return paramContext.getResources().getDisplayMetrics().heightPixels;
    }

    /*
     * 打开设置网络界面
     * */
    public static void networkSet(final Context context){
        // TODO Auto-generated method stub
        Intent intent=null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if(Build.VERSION.SDK_INT>10){
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    public static File downLoadFilePNG(String url) {
        // TODO Auto-generated method stub
        String[] names= url.split("/");
        if(names.length == 0){
            return null;
        }
        final String fileName =  names[names.length -1];
        final String dirName = Environment.getExternalStorageDirectory()+ "/icons/";
        File tmpFile = new File(dirName);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }

        final File file = new File(dirName + fileName);
        try {
            URL urlc = new URL(url);
            try {
                HttpURLConnection conn = (HttpURLConnection) urlc
                        .openConnection();
                if(file.exists() && file.length() == conn.getContentLength()){
                    conn.disconnect();
                    conn = null;
                    return file;
                }

                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(file);
                byte[] bs = new byte[1024];
                int len;
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }

                conn.disconnect();
                conn = null;
                os.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void setSimulateClick(ViewGroup viewGroup, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 100;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        viewGroup.dispatchTouchEvent(downEvent);
        viewGroup.dispatchTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
        Log.i("dd", "dd");
    }




}
