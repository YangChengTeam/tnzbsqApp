package com.yc.loanbox.helper;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        try {

            Glide.with(context).load(path).into(imageView);
        } catch (Exception e){
            Log.e("BannerImageLoader",  e.getMessage());
        }
    }
}
