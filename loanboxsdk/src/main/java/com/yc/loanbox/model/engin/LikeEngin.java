package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.SortsInfo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class LikeEngin extends BaseEngin<ResultInfo<List<ProductInfo>>>  {
    public LikeEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.YOU_LIKE_LIST_URL;
    }


    public Observable<ResultInfo<List<ProductInfo>>> getLikeInfo() {
        return rxpost(new TypeReference<ResultInfo<List<ProductInfo>>>() {
        }.getType(), null, true, true, true);
    }
}
