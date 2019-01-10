package com.yc.loanbox.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.IndexInfo;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class SearchEngin extends BaseEngin<ResultInfo<List<ProductInfo>>> {
    public SearchEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.SEARCH_RESULT_URL;
    }

    public Observable<ResultInfo<List<ProductInfo>>> getSearchInfo(String money_min, String money_max, String time_min, String time_max, String class_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("money_min", money_min);
        params.put("money_max", money_max);
        params.put("time_min", time_min);
        params.put("time_max", time_max);
        params.put("class_id", class_id);
        return rxpost(new TypeReference<ResultInfo<List<ProductInfo>>>() {
        }.getType(), params, true, true, true);
    }

}
