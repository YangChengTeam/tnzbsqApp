package com.fy.tnzbsq.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AndroidToastForJs {

	private Context mContext;

//	public AndroidToastForJs(Context context) {
//		this.mContext = context;
//	}

	// webview中调用toast原生组件
	@JavascriptInterface
	public void showToast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}

	// webview中求和
	@JavascriptInterface
	public int sum(int a, int b) {
		return a + b;
	}

	// 以json实现webview与js之间的数据交互
	@JavascriptInterface
	public String jsontohtml() {
		JSONObject map;
		JSONArray array = new JSONArray();
		try {
			map = new JSONObject();
			map.put("name", "aaron");
			map.put("age", 25);
			map.put("address", "中国上海");
			array.put(map);

			map = new JSONObject();
			map.put("name", "jacky");
			map.put("age", 22);
			map.put("address", "中国北京");
			array.put(map);

			map = new JSONObject();
			map.put("name", "vans");
			map.put("age", 26);
			map.put("address", "中国深圳");
			map.put("phone", "13888888888");
			array.put(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array.toString();
	}
}
