package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.NewListInfo;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class NewMouthEngin extends BaseEngin {

    public NewMouthEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.NEW_LIST_URL;
    }

    public Observable<ResultInfo<NewListInfo>> getNewList() {
        return rxpost(new TypeReference<ResultInfo<NewListInfo>>() {
        }.getType(), null, true, true, true);
    }
}
