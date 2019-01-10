package com.yc.loanbox.model.bean;

public class SortInfo {
    private String id;
    private String name;
    private String value_min;
    private String value_max;

    public String getValue_min() {
        return value_min;
    }

    public void setValue_min(String value_min) {
        this.value_min = value_min;
    }

    public String getValue_max() {
        return value_max;
    }

    public void setValue_max(String value_max) {
        this.value_max = value_max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
