package com.yc.loanbox.view;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tencent.mmkv.MMKV;
import com.yc.loanbox.model.engin.ClickEngin;

public class AndroidInterface {

    private Context context;
    private String id;

    public AndroidInterface(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @JavascriptInterface
    public void setPhone(String phone, String url){
        MMKV.defaultMMKV().putString("phone", phone);
        new ClickEngin(context).click(id, "1", phone, url).subscribe();
    }
}
