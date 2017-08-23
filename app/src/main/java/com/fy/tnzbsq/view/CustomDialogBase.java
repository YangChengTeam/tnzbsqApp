package com.fy.tnzbsq.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class CustomDialogBase extends Dialog {
	private View contentView;

	public CustomDialogBase(Context context) {
		super(context);
	}

	public CustomDialogBase(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(View contentView) {
		super.setContentView(contentView);
		this.contentView = contentView;
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		contentView = view;
	}
}
