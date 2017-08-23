package com.fy.tnzbsq.adapter;

import org.kymjs.kjframe.utils.DensityUtils;

import com.fy.tnzbsq.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BubbleAdapter extends BaseAdapter {
	private static final String TAG = "HotWordsAdapter";
	private Context mContext;
	private int[] dataList;

	private int selectedPosition = 0;// 选中的位置
	
	public BubbleAdapter(Context mContext, int[] dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
	}

	public void clear() {
		// dataList.clear();
	}

	@Override
	public int getCount() {
		return dataList.length;
	}

	@Override
	public Object getItem(int pos) {
		return dataList[pos];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView -> position = " + position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.bubble_item, null);
			holder.bubbleLayout = (LinearLayout) convertView.findViewById(R.id.bubble_layout);
			holder.bubbleIv = (ImageView) convertView.findViewById(R.id.bubble_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		
		
		//版本高于4.1之上
		if (Build.VERSION.SDK_INT >= 16) {
			if (selectedPosition == position) {
				holder.bubbleLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bubble_selector));
	        } else {
	        	holder.bubbleLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bubble_normal));
	        }  
		} else {
			if (selectedPosition == position) {
				holder.bubbleLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bubble_selector));
	        } else {
	        	holder.bubbleLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bubble_normal));
	        }  
		}
		
		
		
		holder.bubbleIv.setBackgroundResource(dataList[position]);
		
		/*Drawable dw = mContext.getResources().getDrawable(dataList[position]);
		ViewGroup.LayoutParams lp = holder.bubbleIv.getLayoutParams();
		lp.width = DensityUtils.dip2px(mContext, 45);
		lp.height = (int) (DensityUtils.dip2px(mContext, 45)/((double)dw.getIntrinsicWidth()/(double)dw.getIntrinsicHeight()));
		holder.bubbleIv.setLayoutParams(lp);*/
		
		return convertView;
	}

	class ViewHolder {
		LinearLayout bubbleLayout;
		ImageView bubbleIv;
	}

}
