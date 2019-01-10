package com.yc.loanbox.model.bean;

import java.util.List;

public class IndexInfo {

    private List<ProductInfo> hot_list;

    private List<ProductInfo> youlike_list;

    private List<ProductInfo> banner_list;

    private List<RollInfo> app_msg_list;

    private ProductInfo ad_left_info;

    private ProductInfo ad_right_info;

    private List<TypeInfo> type_list;

    private ProductInfo ad_roll_info;

    private ProductInfo open_window_ad;

    public ProductInfo getOpen_window_ad() {
        return open_window_ad;
    }

    public void setOpen_window_ad(ProductInfo open_window_ad) {
        this.open_window_ad = open_window_ad;
    }

    public ProductInfo getAd_roll_info() {
        return ad_roll_info;
    }

    public void setAd_roll_info(ProductInfo ad_roll_info) {
        this.ad_roll_info = ad_roll_info;
    }

    public List<TypeInfo> getType_list() {
        return type_list;
    }

    public void setType_list(List<TypeInfo> type_list) {
        this.type_list = type_list;
    }


    public List<RollInfo> getApp_msg_list() {
        return app_msg_list;
    }

    public void setApp_msg_list(List<RollInfo> app_msg_list) {
        this.app_msg_list = app_msg_list;
    }

    public List<ProductInfo> getHot_list() {
        return hot_list;
    }

    public void setHot_list(List<ProductInfo> hot_list) {
        this.hot_list = hot_list;
    }

    public List<ProductInfo> getYoulike_list() {
        return youlike_list;
    }

    public void setYoulike_list(List<ProductInfo> youlike_list) {
        this.youlike_list = youlike_list;
    }

    public List<ProductInfo> getBanner_list() {
        return banner_list;
    }

    public void setBanner_list(List<ProductInfo> banner_list) {
        this.banner_list = banner_list;
    }

    public ProductInfo getAd_left_info() {
        return ad_left_info;
    }

    public void setAd_left_info(ProductInfo ad_left_info) {
        this.ad_left_info = ad_left_info;
    }

    public ProductInfo getAd_right_info() {
        return ad_right_info;
    }

    public void setAd_right_info(ProductInfo ad_right_info) {
        this.ad_right_info = ad_right_info;
    }



}
