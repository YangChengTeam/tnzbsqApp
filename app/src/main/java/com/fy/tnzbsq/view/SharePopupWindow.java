package com.fy.tnzbsq.view;

import com.fy.tnzbsq.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * 从底部弹出或滑出选择菜单或窗口
 * @author admin
 *
 */
public class SharePopupWindow extends PopupWindow {

	//private Button takePhotoBtn, pickPhotoBtn, cancelBtn;
	private View mMenuView;
	
	private RelativeLayout qq_layout;
	
	private RelativeLayout qzone_layout;
	
	private RelativeLayout wechat_layout;
	
	private RelativeLayout wxcircle_layout;
	
	private LinearLayout cancelLayout;

	@SuppressLint("InflateParams")
	public SharePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_window_share, null);
		
		qq_layout = (RelativeLayout)mMenuView.findViewById(R.id.qq_layout);
		qzone_layout = (RelativeLayout)mMenuView.findViewById(R.id.qzone_layout);
		wechat_layout = (RelativeLayout)mMenuView.findViewById(R.id.wechat_layout);
		wxcircle_layout = (RelativeLayout)mMenuView.findViewById(R.id.wxcircle_layout);
		cancelLayout = (LinearLayout)mMenuView.findViewById(R.id.cancel_layout);

		qq_layout.setOnClickListener(itemsOnClick);
		qzone_layout.setOnClickListener(itemsOnClick);
		wechat_layout.setOnClickListener(itemsOnClick);
		wxcircle_layout.setOnClickListener(itemsOnClick);
		cancelLayout.setOnClickListener(itemsOnClick);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}