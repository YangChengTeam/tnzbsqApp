package com.yc.loanbox.model.bean;

import android.text.TextUtils;

public class ProductInfo {
    private String id;

    private String name;

    private String ico;

    private String ptype;


    private String desp;

    private String reg_url;

    private String money_limit_min;
    private String money_limit_max;

    private String special_flag;

    private String succ_num;

    private String desp2;

    private String image;

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    private String ad_id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSucc_num() {
        return succ_num;
    }

    public void setSucc_num(String succ_num) {
        this.succ_num = succ_num;
    }

    public String getDesp2() {
        return desp2;
    }

    public void setDesp2(String desp2) {
        this.desp2 = desp2;
    }

    public String getSpecial_flag() {
        return special_flag;
    }

    public void setSpecial_flag(String special_flag) {
        this.special_flag = special_flag;
    }

    private String time_limit_min;
    private String time_limit_max;

    public String getMoney_limit_min() {
        return money_limit_min;
    }

    public void setMoney_limit_min(String money_limit_min) {
        this.money_limit_min = money_limit_min;
    }

    public String getTime_limit_min() {
        return time_limit_min;
    }

    public void setTime_limit_min(String time_limit_min) {
        this.time_limit_min = time_limit_min;
    }

    public String getTime_limit_max() {
        return time_limit_max;
    }

    public void setTime_limit_max(String time_limit_max) {
        this.time_limit_max = time_limit_max;
    }

    public String getDay_rate() {
        return day_rate;
    }

    public void setDay_rate(String day_rate) {
        this.day_rate = day_rate;
    }

    private String day_rate;


    public String getMoney_limit_max() {
        return money_limit_max;
    }

    public void setMoney_limit_max(String money_limit_max) {
        this.money_limit_max = money_limit_max;
    }

    public String getId() {
        return id;
    }

    public String getAd_ico() {
        return ad_ico;
    }

    public void setAd_ico(String ad_ico) {
        this.ad_ico = ad_ico;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    private String ad_ico;
    private String ad_name;

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getReg_url() {
        return reg_url;
    }

    public void setReg_url(String reg_url) {
        this.reg_url = reg_url;
    }

    public String getPtype() {
        if(TextUtils.isEmpty(ptype)){
            ptype = "0";
        }
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }


}
