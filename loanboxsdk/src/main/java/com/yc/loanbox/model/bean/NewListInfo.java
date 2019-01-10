package com.yc.loanbox.model.bean;

public class NewListInfo {

    private DayInfo today;

    private DayInfo tomorrow;

    private String total_num;

    private String surplus_num;

    private ListInfo new_list;

    public DayInfo getToday() {
        return today;
    }

    public void setToday(DayInfo today) {
        this.today = today;
    }

    public DayInfo getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(DayInfo tomorrow) {
        this.tomorrow = tomorrow;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getSurplus_num() {
        return surplus_num;
    }

    public void setSurplus_num(String surplus_num) {
        this.surplus_num = surplus_num;
    }

    public ListInfo getNew_list() {
        return new_list;
    }

    public void setNew_list(ListInfo new_list) {
        this.new_list = new_list;
    }
}
