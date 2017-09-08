package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

public class ZBDataInfo implements Serializable {
    public String cid;
    public String id;
    public String title;
    public String class_id;
    public String sub_title;
    public String tags;
    public String front_img;
    public String small_img;
    public String desp;
    public String build_num;
    public String flag;
    public String c_title;
    public String c_sub_title;
    public String c_img;
    public String type;
    public String hot;
    public String collect_num;
    public String add_time;
    public String sort;
    public int is_vip;
    public String price;
    public String name;
    public String seven;
    public String sc;
    
    public List<ZBDataTemplateInfo> template;

    public List<ZBDataFieldInfo> field;

    public List<ZBDataTemplateInfo> getTemplate() {
        return template;
    }

    public void setTemplate(List<ZBDataTemplateInfo> template) {
        this.template = template;
    }

    public List<ZBDataFieldInfo> getField() {
        return field;
    }

    public void setField(List<ZBDataFieldInfo> field) {
        this.field = field;
    }
}