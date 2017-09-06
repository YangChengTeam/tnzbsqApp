package com.fy.tnzbsq.bean;

import java.io.Serializable;
import java.util.List;

public class ZBDataFieldInfo implements Serializable {
    public String id;
    public String name;
    public String temp_id;
    public String type;
    public String restrain;
    public String font_name;
    public String font_color;
    public String font_size;
    public String rotate;
    public int is_hide;
    public int input_type;
    public String def_val;
    public String val;
    public String text_len_limit;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public String orientation;
    public String align;
    public String offset_x;
    public String offset_y;
    public String sort;

    public List<ZBDataFieldSelectInfo> select;

    public List<ZBDataFieldSelectInfo> getSelect() {
        return select;
    }

    public void setSelect(List<ZBDataFieldSelectInfo> select) {
        this.select = select;
    }
}