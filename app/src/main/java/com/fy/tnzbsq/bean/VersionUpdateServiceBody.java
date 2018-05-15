package com.fy.tnzbsq.bean;

import java.io.Serializable;

public class VersionUpdateServiceBody extends Result implements Serializable {
	private static final long serialVersionUID = 1L;
	public String version;
	public String download;
	public String updateLog;
	public String versionCode;
	public int isForce;//0不强制，1强制
}