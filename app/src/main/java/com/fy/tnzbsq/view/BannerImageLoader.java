package com.fy.tnzbsq.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        try {
            imageView.setBackgroundColor(Color.parseColor("#e0eaf4"));
            Glide.with(context).load(path).into(imageView);
        }catch (Exception e){
            Log.e("BannerImageLoader",  e.getMessage());
        }
    }
}
