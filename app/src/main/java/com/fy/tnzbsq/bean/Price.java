package com.fy.tnzbsq.bean;

import com.tgb.lk.ahibernate.annotation.Column;
import com.tgb.lk.ahibernate.annotation.Table;

import java.io.Serializable;

@Table(name = "price")
public class Price implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "single")
	public String single;

	@Column(name = "vip")
	public String vip;

	@Column(name = "singledesp")
	public String singledesp;

	@Column(name = "vipdesp")
	public String vipdesp;

}
