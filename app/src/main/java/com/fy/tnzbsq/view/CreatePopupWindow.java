package com.fy.tnzbsq.view;

import org.kymjs.kjframe.utils.DensityUtils;

import com.fy.tnzbsq.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 从底部弹出或滑出选择菜单或窗口
 * 
 */
public class CreatePopupWindow extends PopupWindow {

	private View mMenuView;

	private RelativeLayout create_layout;
	
	private LinearLayout close_layout;

	private LinearLayout create_image_layout;

	private LinearLayout create_word_layout;

	private Animation translateAnimationLeft;

	private Animation translateAnimationRight;
	
	private ImageView createImg;

	private ImageView createWord;
	
	private TextView create_img_tv;
	
	private TextView create_word_tv;
	
	private int fromXDeltaLeft;
	
	private int fromXDeltaRight;
	
	private int fromYDelta;
	
	@SuppressLint("InflateParams")
	public CreatePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_window_create, null);

		create_img_tv = (TextView) mMenuView.findViewById(R.id.create_img_tv);
		create_word_tv = (TextView) mMenuView.findViewById(R.id.create_word_tv);
		create_img_tv.getPaint().setFakeBoldText(true);
		create_word_tv.getPaint().setFakeBoldText(true);
		createImg = (ImageView) mMenuView.findViewById(R.id.create_img);
		createWord = (ImageView) mMenuView.findViewById(R.id.create_word);
		// translateAnimation = AnimationUtils.loadAnimation(context,
		// R.anim.create_anim);
		
		create_layout = (RelativeLayout) mMenuView.findViewById(R.id.create_layout);
		close_layout = (LinearLayout) mMenuView.findViewById(R.id.close_layout);
		create_image_layout = (LinearLayout) mMenuView.findViewById(R.id.create_image_layout);
		create_word_layout = (LinearLayout) mMenuView.findViewById(R.id.create_word_layout);
		
		close_layout.setOnClickListener(itemsOnClick);
		create_image_layout.setOnClickListener(itemsOnClick);
		create_word_layout.setOnClickListener(itemsOnClick);
		
		fromXDeltaLeft = DensityUtils.getScreenW(context) / 2 - DensityUtils.dip2px(context, 150) / 2;
		fromXDeltaRight = -(DensityUtils.getScreenW(context) / 2 - DensityUtils.dip2px(context, 150) / 2);
		
		fromYDelta = 500 - create_image_layout.getHeight();
		int toYDelta = 500 - create_image_layout.getHeight() - 150;

		translateAnimationLeft = new TranslateAnimation(fromXDeltaLeft, 80, fromYDelta, 80);
		translateAnimationRight = new TranslateAnimation(fromXDeltaRight, -80, fromYDelta, 80);
		
		//translateAnimationLeft = AnimationUtils.loadAnimation(context, R.anim.my_anim);
		
		translateAnimationLeft.setInterpolator(new BounceInterpolator());
		translateAnimationRight.setInterpolator(new BounceInterpolator());
		translateAnimationLeft.setDuration(1500);
		translateAnimationRight.setDuration(1500);
		
		create_image_layout.startAnimation(translateAnimationLeft);
		create_word_layout.startAnimation(translateAnimationRight);
		translateAnimationLeft.setFillAfter(true);
		translateAnimationRight.setFillAfter(true);
		
		
		create_image_layout.setVisibility(View.VISIBLE);
		create_word_layout.setVisibility(View.VISIBLE);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.create_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						//dismiss();
						closePopwindow();
					}
				}
				return true;
			}
		});

	}
	
	Handler handler=new Handler();
	
	Runnable runnable=new Runnable(){
	   @Override  
	   public void run() {  
		   dismiss();
	   }   
	};  
	
	public void closePopwindow(){
		translateAnimationLeft = new TranslateAnimation(80,fromXDeltaLeft,80,fromYDelta);
		translateAnimationRight = new TranslateAnimation(-80, fromXDeltaRight, 80,fromYDelta);
		
		//translateAnimationLeft = AnimationUtils.loadAnimation(context, R.anim.my_anim);
		
		translateAnimationLeft.setInterpolator(new AccelerateDecelerateInterpolator());
		translateAnimationRight.setInterpolator(new AccelerateDecelerateInterpolator());
		translateAnimationLeft.setDuration(500);
		translateAnimationRight.setDuration(500);
		
		create_image_layout.startAnimation(translateAnimationLeft);
		create_word_layout.startAnimation(translateAnimationRight);
		translateAnimationLeft.setFillAfter(true);
		translateAnimationRight.setFillAfter(true);
		
		create_image_layout.setVisibility(View.GONE);
		create_word_layout.setVisibility(View.GONE);
		handler.postDelayed(runnable, 500);
	}
	
}