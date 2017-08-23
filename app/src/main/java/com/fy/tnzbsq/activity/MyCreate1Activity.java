package com.fy.tnzbsq.activity;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.fragment.CreateActsFragment;
import com.fy.tnzbsq.view.PagerSlidingTabStrip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCreate1Activity extends FragmentActivity {

	private CreateActsFragment homeFragment1;
	
	private CreateActsFragment homeFragment2;
	
	private PagerSlidingTabStrip tabs;
	
	private DisplayMetrics dm;
	
	private TextView titleTv;
	
	private ImageView backImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.my_create2, null);
		backImg = (ImageView)view.findViewById(R.id.back_img);
		
		backImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		setContentView(view);
		initView(view);
	}

	
	private void initView(View view) {
		dm = getResources().getDisplayMetrics();
		titleTv = (TextView)view.findViewById(R.id.top_title);
		titleTv.setText(getResources().getString(R.string.my_create_text));
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();
		
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// tabs.setDividerColor(Color.BLACK);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 5, dm));// 4
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm)); // 16
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#FFA07A"));// @color/red
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#FFA07A"));// @color/red
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	// FragmentPagerAdapter FragmentStatePagerAdapter //不能用FragmentPagerAdapter

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "装逼", "斗图"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (null == homeFragment1) {
					homeFragment1 = new CreateActsFragment();
				}
				return homeFragment1;
			case 1:
				if (null == homeFragment2) {
					homeFragment2 = new CreateActsFragment();
				}
				return homeFragment2;
			default:
				return null;
			}
		}
	}
}
