package com.fy.tnzbsq.bean;

/**
 * Created by admin on 2017/12/17.
 */

public class WeiXinInfoRet {
    public int errCode;
    public boolean status;
    public String message;
    public WeiXinInfo data;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WeiXinInfo getData() {
        return data;
    }

    public void setData(WeiXinInfo data) {
        this.data = data;
    }
}
