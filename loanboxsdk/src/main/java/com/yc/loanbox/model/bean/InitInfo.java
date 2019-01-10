package com.yc.loanbox.model.bean;

public class InitInfo {
    private UserInfo userInfo;

    private ProductInfo init_img;

    public ProductInfo getInit_img() {
        return init_img;
    }

    public void setInit_img(ProductInfo init_img) {
        this.init_img = init_img;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
