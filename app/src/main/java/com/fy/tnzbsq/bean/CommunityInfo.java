package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 互动社区-帖子信息
 */
public class CommunityInfo implements Serializable {

    public String id;

    public String user_id;

    public String user_name;

    public String face;

    public String content;

    public String notice;

    public String type;

    public String flag;

    public String sort;

    public String follow_count;

    public String agree_count;

    public String status;

    public String add_time;

    public List<String> images;

    public String agreed;
}
