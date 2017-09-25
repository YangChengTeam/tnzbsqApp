package com.fy.tnzbsq.net;


import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by admin on 2016/9/6.
 */
public class OKHttpRequest {

    public void aget(String url, Map<String, String> params, final OnResponseListener onResponseListener) {

        OkHttpUtils.post().params(params).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("---data error---");
                onResponseListener.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.e("--- data success---" + response);
                if (onResponseListener != null) {
                    onResponseListener.onSuccess(response);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                onResponseListener.onBefore();
            }
        });
    }

    public void aget(String url, Map<String, String> params, File upFile, final OnResponseListener onResponseListener) {
        String fileName = Contants.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
        OkHttpUtils.post().addHeader("Cookie", "cookie_tnzbsq").params(params).addFile("img", fileName, upFile).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("---data error---");
                onResponseListener.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                //Logger.e("--- data success---" + response);
                if (onResponseListener != null) {
                    onResponseListener.onSuccess(response);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                onResponseListener.onBefore();
            }
        });
    }

    public void aget(String url, Map<String, String> params, List<File> files, final OnResponseListener onResponseListener) {

        PostFormBuilder postFormBuilder = OkHttpUtils.post().addHeader("Cookie", "cookie_tnzbsq").params(params).url(url);
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                String fileName = Contants.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
                postFormBuilder.addFile("img"+ (i + 1), fileName, files.get(i));
            }
        }

        postFormBuilder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e("---data error---");
                onResponseListener.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                //Logger.e("--- data success---" + response);
                if (onResponseListener != null) {
                    onResponseListener.onSuccess(response);
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                onResponseListener.onBefore();
            }
        });
    }


}

