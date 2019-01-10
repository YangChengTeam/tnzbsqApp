package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.CodeInfo;

import java.util.HashMap;

import rx.Observable;

public class SmsEngin extends BaseEngin<ResultInfo<CodeInfo>> {
    public SmsEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.SEND_CODE_URL;
    }

    public Observable<ResultInfo<CodeInfo>> sendSms(String phone){
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        return rxpost(new TypeReference<ResultInfo<CodeInfo>>() {
        }.getType(), params, true, true, true);
    }
}
