package com.fy.tnzbsq.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断当前手机联网的渠道
 * 
 * @author Administrator
 * 
 */
public class NetUtil
{
	
	private static NetworkInfo networkInfo = null;
	
	/**
	 * 检查当前手机网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context)
	{
		// 判断连接方式
		boolean wifiConnected = isWIFIConnected(context);
		boolean mobileConnected = isMOBILEConnected(context);
		if (wifiConnected == false && mobileConnected == false)
		{
			// 如果都没有连接返回false，提示用户当前没有网络
			return false;
		}
		return true;
	}

	// 判断手机使用是wifi还是mobile
	/**
	 * 判断手机是否采用wifi连接
	 */
	public static boolean isWIFIConnected(Context context)
	{
		// Context.CONNECTIVITY_SERVICE).

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		return false;
	}

	public static boolean isMOBILEConnected(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断网络是否可用
	 * 
	 * @return 是/否
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (null == manager) {
			return false;
		}
		networkInfo = manager.getActiveNetworkInfo();
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断网络是否已连接
	 * 
	 * @return 是/否
	 */
	public static boolean isConnected(Context context) {
		if (!isAvailable(context)) {
			return false;
		}
		if (!networkInfo.isConnected()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断GPS是否打开
	 * 
	 * @return
	 */
	public static boolean isGpsOPen(Context context) {
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean isGpsOkay = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isGpsOkay) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/*------------------上传头像------------*/
	
	public static final String BOUNDARY = "--my_boundary--";

	/**
	 * 普通字符串数据
	 * @param textParams
	 * @param ds
	 * @throws Exception
	 */
	public static void writeStringParams(Map<String, String> textParams,
			DataOutputStream ds) throws Exception {
		Set<String> keySet = textParams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParams.get(name);
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
			ds.writeBytes("\r\n");
			value = value + "\r\n";
			ds.write(value.getBytes());

		}
	}

	/**
	 * 文件数据
	 * @param fileparams
	 * @param ds
	 * @throws Exception
	 */
	public static void writeFileParams(Map<String, File> fileparams, 
			DataOutputStream ds) throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = fileparams.get(name);
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
					+ URLEncoder.encode(value.getName(), "UTF-8") + "\"\r\n");
			ds.writeBytes("Content-Type:application/octet-stream \r\n");
			ds.writeBytes("\r\n");
			ds.write(getBytes(value));
			ds.writeBytes("\r\n");
		}
	}

	// 把文件转换成字节数组
	private static byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}

	/**
	 * 添加结尾数据
	 * @param ds
	 * @throws Exception
	 */
	public static void paramsEnd(DataOutputStream ds) throws Exception {
		ds.writeBytes("--" + BOUNDARY + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}

	public static String readString(InputStream is) {
		return new String(readBytes(is));
	}

	public static byte[] readBytes(InputStream is) {
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	// url检测
	public static boolean is404NotFound(String urlStr){
		boolean result = false;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			int responseCode = httpURLConnection.getResponseCode();
			httpURLConnection.disconnect();
			result =  responseCode != HttpURLConnection.HTTP_OK ? true : false;
		}catch (Exception e){
			result = true;
		}
		return result;
	}
}
