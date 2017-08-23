package com.fy.tnzbsq.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.bean.HotWordListRet;
import com.fy.tnzbsq.bean.ImageCreateRet;
import com.fy.tnzbsq.bean.MyCreateRet;
import com.fy.tnzbsq.bean.UserRet;
import com.fy.tnzbsq.bean.VersionUpdateServiceRet;
import com.fy.tnzbsq.bean.WordCreateRet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Context;

public class ServiceInterface {
	/*
	 * public final static Gson gson = new GsonBuilder()
	 * .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
	 * .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	 */
	public final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
			.create();

	public static UserRet login(Context context, String username, String password) {

		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("username", username));
		list.add(new BasicNameValuePair("password", password));
		String json = NetConnection.post(context, Server.URL_LOGIN, list);

		return gson.fromJson(json, new TypeToken<UserRet>() {
		}.getType());
	}

	public static VersionUpdateServiceRet VersionUpdateService(Context context, String channel) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("channel", channel));
		String json = NetConnection.post(context, Server.URL_VERSION_UPDATE, list);
		try {
			return gson.fromJson(json, new TypeToken<VersionUpdateServiceRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加载所有装逼的数据
	 * 
	 * @param context
	 * @return
	 */
	public static String getAllData(Context context) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", App.ANDROID_ID));
		String json = NetConnection.post(context, Server.URL_ALL_DATA, list);
		return json;
	}

	/**
	 * 根据最后一个ID,加载比这个ID大的所有的装逼的数据
	 *
	 * @param context
	 * @return
	 */
	public static String getAllDataByLastId(Context context,String dataId) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", App.ANDROID_ID));
		list.add(new BasicNameValuePair("dataId", dataId));
		String json = NetConnection.post(context, Server.URL_ALL_DATA_BY_ID, list);
		return json;
	}

	/**
	 * 加载所有斗图的数据
	 * 
	 * @param context
	 * @return
	 */
	public static String getAllFightData(Context context) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", App.ANDROID_ID));
		String json = NetConnection.post(context, Server.URL_ALL_FIGHT_DATA, list);
		return json;
	}

	/**
	 * 图片生成
	 * 
	 * @param context
	 * @return
	 */
	public static ImageCreateRet ImageCreateService(Context context, String id, String mime, String requestData) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", id));
		list.add(new BasicNameValuePair("mime", mime));
		list.add(new BasicNameValuePair("requestData", requestData));
		String json = NetConnection.post(context, Server.URL_IMAGE_CREATE, list);
		try {
			return gson.fromJson(json, new TypeToken<ImageCreateRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 我的收藏
	 * 
	 * @param context
	 * @return
	 */
	public static String getMykeepData(Context context, String mime) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", mime));
		String json = NetConnection.post(context, Server.URL_MY_KEEP_DATA, list);
		return json;
	}

	/**
	 * 我的生成
	 * 
	 * @param context
	 * @return
	 */
	public static String getMyCreateData(Context context, String mime) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", mime));
		String json = NetConnection.post(context, Server.URL_MY_CREATE_DATA, list);
		return json;
	}

	/**
	 * 添加收藏/取消收藏
	 * 
	 * @param context
	 * @return
	 */
	public static MyCreateRet getAddKeepData(Context context, String id, String mime) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		list.add(new BasicNameValuePair("mid", id));
		list.add(new BasicNameValuePair("mime", mime));
		String json = NetConnection.post(context, Server.URL_ADD_KEEP_DATA, list);
		try {
			return gson.fromJson(json, new TypeToken<MyCreateRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static UserRet updateUser(Context context, String mime, String username) {

		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", mime));
		list.add(new BasicNameValuePair("username", username));
		String json = NetConnection.post(context, Server.URL_UPDATE_USER, list);
		try {
			return gson.fromJson(json, new TypeToken<UserRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取热门搜索
	 * 
	 * @param context
	 * @return
	 */
	public static String getHotData(Context context) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		String json = NetConnection.post(context, Server.URL_GET_HOT_DATA, list);
		return json;
	}

	/**
	 * 加载搜索的数据
	 * 
	 * @param context
	 * @return
	 */
	public static String getSearchData(Context context, String keyword) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("mime", App.ANDROID_ID));
		list.add(new BasicNameValuePair("keyword", keyword));
		String json = NetConnection.post(context, Server.URL_SEARCH_DATA, list);
		return json;
	}

	/**
	 * 文字处理接口
	 * 
	 * @param context
	 * @param mime
	 * @param color
	 * @param wordText
	 * @param size
	 * @param faceType
	 * @return
	 */
	public static WordCreateRet wordCreateData(Context context, String mime, String color, String wordText, String size,
			String faceType) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		list.add(new BasicNameValuePair("mime", mime));
		list.add(new BasicNameValuePair("wcolor", color));
		list.add(new BasicNameValuePair("wtext", wordText));
		list.add(new BasicNameValuePair("wsize", size));
		list.add(new BasicNameValuePair("wfont", faceType));

		String json = NetConnection.post(context, Server.URL_WORD_CREATE_DATA, list);
		try {
			return gson.fromJson(json, new TypeToken<WordCreateRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 根据ID获取斗图的热门词列表
	 * @param context
	 * @param id
	 * @return
	 */
	public static HotWordListRet getHotWordListById(Context context, String id) {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", id));
		String json = NetConnection.post(context, Server.URL_HOT_WORD_LIST_DATA, list);
		try {
			return gson.fromJson(json, new TypeToken<HotWordListRet>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
