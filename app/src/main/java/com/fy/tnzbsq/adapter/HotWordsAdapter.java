package com.fy.tnzbsq.adapter;

import java.util.List;

import com.fy.tnzbsq.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotWordsAdapter extends BaseAdapter {
	private static final String TAG = "HotWordsAdapter";
	private Context mContext;
	private List<String> dataList;

	public HotWordsAdapter(Context mContext, List<String> dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
	}

	public void add(String data) {
		this.dataList.add(data);
	}

	public void addAll(List<String> dataList) {
		this.dataList.addAll(dataList);
	}

	public void clear() {
		dataList.clear();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int pos) {
		return dataList.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView -> position = " + position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_words_item, null);
			holder.hotContentTv = (TextView) convertView.findViewById(R.id.hot_content_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.hotContentTv.setText(dataList.get(position));
		
		return convertView;
	}

	class ViewHolder {
		TextView hotContentTv;
	}

}
