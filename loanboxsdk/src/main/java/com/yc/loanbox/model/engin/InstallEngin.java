package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.constant.Config;

import java.util.HashMap;

import rx.Observable;

public class InstallEngin extends BaseEngin {
    public InstallEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.APP_INSTALL_URL;
    }

    public Observable<ResultInfo<Void>> install(String app_package_name, String app_name, String type){

        HashMap<String, String> params = new HashMap<>();
        params.put("app_package_name", app_package_name);
        params.put("type", type);
        params.put("app_name", app_name);
        if(LoanApplication.getLoanApplication().channelInfo != null) {
            params.put("soft_id", LoanApplication.getLoanApplication().channelInfo.getSoft_id());
            params.put("site_id", LoanApplication.getLoanApplication().channelInfo.getSite_id());
        }
        return rxpost(new TypeReference<ResultInfo<Void>>() {
        }.getType(), params, true, true, true);
    }
}
