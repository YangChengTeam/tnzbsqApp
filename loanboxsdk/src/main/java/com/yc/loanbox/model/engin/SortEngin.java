package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.SortsInfo;

import rx.Observable;

public class SortEngin extends BaseEngin<ResultInfo<SortsInfo>> {
    public SortEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.SORT_URL;
    }

    public Observable<ResultInfo<SortsInfo>> getSortInfo() {
        return rxpost(new TypeReference<ResultInfo<SortsInfo>>() {
        }.getType(), null, true, true, true);
    }
}
