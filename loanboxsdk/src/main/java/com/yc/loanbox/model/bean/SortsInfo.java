package com.yc.loanbox.model.bean;

import java.util.List;

public class SortsInfo {
    public List<SortInfo> getMoney() {
        return money;
    }

    public void setMoney(List<SortInfo> money) {
        this.money = money;
    }

    public List<SortInfo> getTime() {
        return time;
    }

    public void setTime(List<SortInfo> time) {
        this.time = time;
    }

    public List<SortInfo> getClass2() {
        return class2;
    }

    public void setClass2(List<SortInfo> class2) {
        this.class2 = class2;
    }

    private List<SortInfo> money;
    private List<SortInfo> time;
    private List<SortInfo> class2;
}
