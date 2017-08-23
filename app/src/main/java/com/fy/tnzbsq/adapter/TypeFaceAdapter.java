package com.fy.tnzbsq.adapter;

import com.fy.tnzbsq.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TypeFaceAdapter extends BaseAdapter {
	private static final String TAG = "HotWordsAdapter";
	private Context mContext;
	private int[] dataList;

	private int selectedPosition = 0;// 选中的位置

	public TypeFaceAdapter(Context mContext, int[] dataList) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.type_face_item, null);
			holder.typefaceLayout = (LinearLayout) convertView.findViewById(R.id.typeface_layout);
			holder.typefaceIv = (ImageView) convertView.findViewById(R.id.typeface_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//版本高于4.1之上
		if (Build.VERSION.SDK_INT >= 16) {
			if (selectedPosition == position) {
				holder.typefaceLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.type_face_selector));
			} else {
				holder.typefaceLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.type_face_normal));
			}
		} else {
			if (selectedPosition == position) {
				holder.typefaceLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.type_face_selector));
			} else {
				holder.typefaceLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.type_face_normal));
			}
		}

		holder.typefaceIv.setBackgroundResource(dataList[position]);
		return convertView;
	}

	class ViewHolder {
		LinearLayout typefaceLayout;
		ImageView typefaceIv;
	}

}
