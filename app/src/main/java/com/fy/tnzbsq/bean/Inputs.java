package com.fy.tnzbsq.bean;

import java.io.Serializable;

import com.tgb.lk.ahibernate.annotation.Column;

public class Inputs implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "id")
	public int id;
	@Column(name = "modelid")
	public String modelid;
	@Column(name = "textname")
	public String textname;
	@Column(name = "topposition")
	public String topposition;
	@Column(name = "leftposition")
	public String leftposition;
	@Column(name = "restrain")
	public String restrain;
	@Column(name = "fontname")
	public String fontname;
	@Column(name = "fontcolor")
	public String fontcolor;
	@Column(name = "fontsize")
	public String fontsize;
	@Column(name = "rotatetext")
	public String rotatetext;
	@Column(name = "bindfield")
	public String bindfield;
	@Column(name = "display")
	public String display;
	@Column(name = "deftext")
	public String deftext;
	@Column(name = "textlen")
	public String textlen;

}