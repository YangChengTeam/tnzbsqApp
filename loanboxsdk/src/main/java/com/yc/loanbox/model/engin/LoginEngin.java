package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.UserInfo;


import java.util.HashMap;

import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class LoginEngin extends BaseEngin<ResultInfo<UserInfo>> {
    public LoginEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.LOGIN_URL;
    }

    public Observable<ResultInfo<UserInfo>> login(String phone, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        return rxpost(new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);
    }
}
