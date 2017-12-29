package com.fy.tnzbsq.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/12/28.
 */

public class CreateCardInfo extends Result implements Serializable {
    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
