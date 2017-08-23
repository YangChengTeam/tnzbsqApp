package com.fy.tnzbsq.bean;

import java.io.Serializable;

import com.tgb.lk.ahibernate.annotation.Column;
import com.tgb.lk.ahibernate.annotation.Table;

@Table(name = "user")
public class Acts implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "id")
	public int id;

	@Column(name = "title")
	public String title;

	@Column(name = "classtype")
	public String classtype;

	@Column(name = "stitle")
	public String stitle;

	@Column(name = "tags")
	public String tags;

	@Column(name = "frontimg")
	public String frontimg;

	@Column(name = "smallimg")
	public String smallimg;

	@Column(name = "content")
	public String content;

	@Column(name = "img")
	public String img;

	@Column(name = "num")
	public int num;

	@Column(name = "isbest")
	public int isbest;

	@Column(name = "rmsg")
	public String rmsg;

	@Column(name = "rtitle")
	public String rtitle;

	@Column(name = "bestimg")
	public String bestimg;

	@Column(name = "imgwidth")
	public String imagewidth;

	@Column(name = "imgheight")
	public String imageheight;

	@Column(name = "addtime")
	public String addtime;

	@Column(name = "thumb")
	public String thumb;

	@Column(name = "sc")
	public String sc;

	@Column(name = "collect")
	public String collect;

	//@Column(name = "inputs")
	//public String inputs;
	
}
