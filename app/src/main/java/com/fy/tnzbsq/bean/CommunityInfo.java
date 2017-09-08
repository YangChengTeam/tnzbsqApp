package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 互动社区-帖子信息
 */
public class CommunityInfo implements Serializable {

    private static final long serialVersionUID = -7060210533600464481L;

    private String id;

    private String user_id;

    private String user_name;

    private String face;

    private String content;

    private List<String> notice;

    private String type;

    private String flag;

    private String sort;

    private String follow_count;

    private String agree_count;

    private String status;

    private String add_time;

    private List<String> images;

    private String agreed;

    public CommunityInfo() {
        super();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getNotice() {
        return notice;
    }

    public void setNotice(List<String> notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(String follow_count) {
        this.follow_count = follow_count;
    }

    public String getAgree_count() {
        return agree_count;
    }

    public void setAgree_count(String agree_count) {
        this.agree_count = agree_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAgreed() {
        return agreed;
    }

    public void setAgreed(String agreed) {
        this.agreed = agreed;
    }
}
