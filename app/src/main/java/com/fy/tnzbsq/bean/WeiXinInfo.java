package com.fy.tnzbsq.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/12/17.
 */

public class WeiXinInfo implements Serializable {
    public int status;
    public String url;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
