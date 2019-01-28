package com.yc.loanbox.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class GlideHelper {

    public static void circleBorderImageView(final Context context, ImageView imageView, String url, int
            placehorder, float borderwidth, int bordercolor, int width, int height) {
        if(width <= 0) {
            Glide.with(context).load(url).placeholder(placehorder).transform(new GlideCircleTransformation(context)).into(imageView);
        } else {
            Glide.with(context).load(url).override(width, height).placeholder(placehorder).transform(new GlideCircleTransformation(context)).into(imageView);
        }
    }

    public static void imageView(final Context context, ImageView imageView, String url, int
            placehorder) {
        Glide.with(context).load(url).placeholder(placehorder).into(imageView);

    }

    public static void imageView(final Context context, ImageView imageView, String url, int
            placehorder, int width, int height) {
        Glide.with(context).load(url).override(width, height).placeholder(placehorder).into(imageView);
    }

}
