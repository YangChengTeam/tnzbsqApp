package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.IndexInfo;

import java.util.HashMap;

import rx.Observable;

public class IndexEngin extends BaseEngin<ResultInfo<IndexInfo>> {

    public IndexEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.Index_URL;
    }

    public Observable<ResultInfo<IndexInfo>> getIndexInfo() {
        HashMap<String, String> params = new HashMap<>();
        return rxpost(new TypeReference<ResultInfo<IndexInfo>>() {
        }.getType(), params, true, true, true);
    }
}
