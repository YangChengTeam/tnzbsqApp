package com.yc.loanbox.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;

import java.util.HashMap;

import rx.Observable;

public class ClickEngin extends BaseEngin<ResultInfo<Void>> {
    public ClickEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.SET_LOAD_NUM_URL;
    }

    public Observable<ResultInfo<Void>> click(String loan_id, String type, String phone, String url, String ad_id, String ptype){
        if(TextUtils.isEmpty(ad_id)){
            ad_id = "";
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("loan_id", loan_id);
        params.put("type", type);
        params.put("phone", phone);
        params.put("url", url);
        params.put("ad_id",  ad_id);
        params.put("ptype",  ptype);
        return rxpost(new TypeReference<ResultInfo<Void>>() {
        }.getType(), params, true, true, true);
    }

    public Observable<ResultInfo<Void>> click(String loan_id, String type, String phone, String url, String ad_id){
        return click(loan_id, type, phone, url, ad_id, "");
    }

    public Observable<ResultInfo<Void>> click(String loan_id, String type, String phone, String url){
        return click(loan_id, type, phone, url, "", "");
    }

    public Observable<ResultInfo<Void>> click(String loan_id, String type, String phone){
       return click(loan_id, type, phone, "", "", "");
    }

    public Observable<ResultInfo<Void>> click(String loan_id, String type){
        return click(loan_id, type, "", "", "", "");
    }

}
