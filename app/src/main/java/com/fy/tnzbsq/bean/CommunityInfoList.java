package com.fy.tnzbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */

public class CommunityInfoList implements Serializable {

    @JSONField(name = "page_count")
    public int pageCount;

    public List<CommunityInfo> list;

    public List<CommunityInfo> getList() {
        return list;
    }

    public void setList(List<CommunityInfo> list) {
        this.list = list;
    }
}
