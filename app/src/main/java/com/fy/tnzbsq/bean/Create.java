package com.fy.tnzbsq.bean;

import java.io.Serializable;

import com.tgb.lk.ahibernate.annotation.Column;
import com.tgb.lk.ahibernate.annotation.Table;

@Table(name = "user")
public class Create implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "id")
	public int id;
	/**
	 * 用户ID
	 */
	@Column(name = "tuserid")
	public String tuserid;
	/**
	 * 生成后图片地址
	 */
	@Column(name = "imgurl")
	public String imgurl;
	/**
	 * 点击次数
	 */
	@Column(name = "num")
	public int num;
	/**
	 * 生成时间
	 */
	@Column(name = "addtime")
	public String addtime;

}
