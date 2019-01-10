package com.yc.loanbox.model.bean;

public class TypeInfo {
    public TypeInfo(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;
    private String ico;
    private String name;
    private int type;

    public TypeInfo(int type, String name, String ico) {
        this.type = type;
        this.name = name;
        this.ico = ico;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIco() {
        return this.ico;
    }

    public void setIco(String image) {
        this.ico = image;
    }
}
