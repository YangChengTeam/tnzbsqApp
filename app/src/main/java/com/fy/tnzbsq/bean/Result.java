package com.fy.tnzbsq.bean;

import java.io.Serializable;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.StatusCode;
import com.fy.tnzbsq.util.AlertUtil;

import android.content.Context;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	public String message;
	public boolean status;
	public String errCode;

	public static boolean checkResult(Context context, Result result) {
		if (result == null) {
			AlertUtil.show(context, R.string.net_connect_error);
			return false;
		}
		if (result.errCode.equals(StatusCode.STATUS_CODE_SUCCESS)) {
			return true;
		} else {
			AlertUtil.show(context, result.message);
			return false;
		}
	}
}
