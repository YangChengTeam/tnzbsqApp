package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.Main5Activity;


/**
 * Created by zhangkai on 2017/7/28.
 */

public class WebPopupWindow extends BasePopupWindow {

    private Dialog loadingDialog;

    @SuppressWarnings("StatementWithEmptyBody")
    @SuppressLint("SetJavaScriptEnabled")
    public WebPopupWindow(Activity context, final String url) {
        super(context);
        loadingDialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        View contextView = inflater.inflate(R.layout.ppw_web, null);

        WebView webView = (WebView) contextView.findViewById(R.id.wv_pay);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HTC D820u Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.1.0.527 U3/0.8.0 Mobile Safari/534.30");
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);

        if ((!Build.MANUFACTURER.toLowerCase().contains("xiaomi")) && (Build.MANUFACTURER.toLowerCase().contains("huawei"))) {

        }
        if (Build.MANUFACTURER.toLowerCase().contains("lenovo"))
            webView.setLayerType(1, null);

        loadingDialog.setTitle("正在打开微信公众号...");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                super.onPageStarted(view, url, favicon);
                openWxpay(url);
            }

            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (openWxpay(url)) {
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });


        webView.loadUrl(url);
    }


    private boolean openWxpay(String url) {
        if (url.startsWith("weixin://")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Main5Activity.getMainActivity().fixOpenwx();
            }
            dismiss();
            return true;
        }
        return false;
    }


    @Override
    public int getLayoutID() {
        return R.layout.ppw_web;
    }

    @Override
    public void dismiss() {
        loadingDialog.dismiss();
        super.dismiss();
    }

}
