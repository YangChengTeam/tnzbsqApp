package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;

import java.util.HashMap;

import rx.Observable;

public class NoticeEngin extends BaseEngin {

    public NoticeEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.NOTICE_URL;
    }


    public Observable<ResultInfo<Void>> notice(int status){
        HashMap<String, String> params = new HashMap<>();
        params.put("notice_status", status+"");
        return rxpost(new TypeReference<ResultInfo<Void>>() {
        }.getType(), params, true, true, true);
    }
}
