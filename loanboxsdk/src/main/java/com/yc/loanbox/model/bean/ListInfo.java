package com.yc.loanbox.model.bean;

import java.util.List;

public class ListInfo {
    private List<ProductInfo> list;

    public List<ProductInfo> getList() {
        return list;
    }

    public void setList(List<ProductInfo> list) {
        this.list = list;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
}
