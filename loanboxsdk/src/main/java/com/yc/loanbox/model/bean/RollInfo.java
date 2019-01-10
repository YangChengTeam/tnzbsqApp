package com.yc.loanbox.model.bean;

public class RollInfo {
    private String roll_msg;
    private ProductInfo loan_info;

    public String getRoll_msg() {
        return roll_msg;
    }

    public void setRoll_msg(String roll_msg) {
        this.roll_msg = roll_msg;
    }

    public ProductInfo getLoan_info() {
        return loan_info;
    }

    public void setLoan_info(ProductInfo loan_info) {
        this.loan_info = loan_info;
    }
}
