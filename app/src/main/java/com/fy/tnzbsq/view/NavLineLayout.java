package com.fy.tnzbsq.view;

import com.fy.tnzbsq.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhangkai on 16/4/6.
 */
public class NavLineLayout extends LinearLayout {

	public NavLineLayout(Context context) {
		this(context, null);
	}

	private TextView titleTV;
	private RelativeLayout back;

	public NavLineLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.nav, this, true);

		titleTV = (TextView) findViewById(R.id.title);
		back = (RelativeLayout) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (delegate != null) {
					delegate.back();
				}
			}
		});
	}

	public void hide() {
		back.setVisibility(View.GONE);
	}

	public void show() {
		back.setVisibility(View.VISIBLE);
	}

	public NavDelegate delegate;

	public interface NavDelegate {
		void back();
	}

	public void setTitle(final String title) {
		titleTV.post(new Runnable() {
			@Override
			public void run() {
				titleTV.setText(title);
			}
		});

	}
}
