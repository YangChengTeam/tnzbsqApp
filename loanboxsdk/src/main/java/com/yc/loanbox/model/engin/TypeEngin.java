package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class TypeEngin extends BaseEngin<ResultInfo<List<ProductInfo>>> {
    public TypeEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.TYPE_URL;
    }

    public Observable<ResultInfo<List<ProductInfo>>> getTypeInfo(String type_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type_id", type_id);

        return rxpost(new TypeReference<ResultInfo<List<ProductInfo>>>() {
        }.getType(), params, true, true, true);
    }
}
