package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

public class GifDataInfo implements Serializable {

    public String title;
    public int imgUrl;

    public GifDataInfo() {
    }

    public GifDataInfo(String title, int imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}