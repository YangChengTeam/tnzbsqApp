package com.fy.tnzbsq.view;

import java.util.ArrayList;
import java.util.List;

import com.fy.tnzbsq.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * Created by zhangkai on 16/4/6.
 */
public class TabLineLayout extends LinearLayout {
	public TabDelegate delegate;

	public interface TabDelegate {
		void tabFragment(int index);
	}

	public TabLineLayout(Context context) {
		this(context, null);
	}

	private List<RelativeLayout> items;

	public TabLineLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab, this, true);

		items = new ArrayList<RelativeLayout>();
		RelativeLayout home = (RelativeLayout) findViewById(R.id.homeRL);
		RelativeLayout add = (RelativeLayout) findViewById(R.id.addRL);
		RelativeLayout fight = (RelativeLayout) findViewById(R.id.fightRL);

		items.add(home);
		items.add(add);
		items.add(fight);

		home.setTag("0");
		add.setTag("1");
		fight.setTag("2");

		TabClickListener clickListener = new TabClickListener();
		home.setOnClickListener(clickListener);
		add.setOnClickListener(clickListener);
		fight.setOnClickListener(clickListener);
	}

	private void clearSelecteds() {
		for (RelativeLayout item : items) {
			item.setBackgroundColor(Color.parseColor("#ffffff"));
			if (item.getChildCount() == 2) {
				ImageView imageView = (ImageView) item.getChildAt(0);
				TextView textView = (TextView) item.getChildAt(1);
				imageView.setSelected(false);
				textView.setTextColor(Color.parseColor("#444444"));
			}
		}
	}

	class TabClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			int index = Integer.parseInt(view.getTag().toString());
			ViewGroup viewGroup = (ViewGroup) view;
			if (viewGroup.getChildCount() == 2) {
				ImageView imageView = (ImageView) viewGroup.getChildAt(0);
				
				if (delegate != null) {
					clearSelecteds();
					delegate.tabFragment(index);
				}
				TextView textView = (TextView) viewGroup.getChildAt(1);
				imageView.setSelected(true);
				textView.setTextColor(Color.parseColor("#fd5200"));
			}
			//点击中间的+号时,用户DIY图片
			if (viewGroup.getChildCount() == 1) {
				if (delegate != null) {
					clearSelecteds();
					delegate.tabFragment(index);
				}
			}
			//viewGroup.setBackgroundColor(Color.parseColor("#e3e3e3"));
		}
	}

	public void check(int index) {
		try {
			ViewGroup item = items.get(index);
			//item.setBackgroundColor(Color.parseColor("#e3e3e3"));
			if (item.getChildCount() == 2) {
				clearSelecteds();
				ImageView imageView = (ImageView) item.getChildAt(0);
				TextView textView = (TextView) item.getChildAt(1);
				imageView.setSelected(true);
				textView.setTextColor(Color.parseColor("#fd5200"));
				if (delegate != null) {
					delegate.tabFragment(index);
				}
			}
		} catch (Exception e) {

		}
	}
}
