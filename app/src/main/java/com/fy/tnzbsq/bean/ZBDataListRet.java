package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

public class ZBDataListRet extends Result implements Serializable {

    public List<ZBDataInfo> data;

    public List<ChannelInfo> channel;

    public List<SlideInfo> banner;
}