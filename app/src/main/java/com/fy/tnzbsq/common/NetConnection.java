package com.fy.tnzbsq.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fy.tnzbsq.util.ImageCacheUtils;
import com.fy.tnzbsq.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetConnection {
	private static final String TAG = NetConnection.class.getSimpleName();

	/**
	 * 默认图片读取超时
	 */
	private final static int DEFAULT_BITMAP_TIMEOUT = 10 * 1000;

	/**
	 * http状态正常值
	 */
	private final static int HTTP_STATE_OK = 200;

	/**
	 * 缓冲大小
	 */
	private final static int BUFFER_SIZE = 1024 * 4;

	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

	/**
	 * get
	 * 
	 * @param urlString
	 * @param params
	 * @return
	 */
	public static String get(Context context, String urlString,
			Map<String, String> params) {
		StringBuilder urlBuilder = new StringBuilder();
		try {
			urlBuilder.append(urlString);
			if (params != null) {
				addSecureParams(params);

				urlBuilder.append("?");
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					urlBuilder
							.append(URLEncoder.encode(param.getKey(), "UTF-8"))
							.append('=')
							.append(URLEncoder.encode(param.getValue(), "UTF-8"));
					if (iterator.hasNext()) {
						urlBuilder.append('&');
					}
				}
			}
			HttpClient client = getNewHttpClient(context);
			Log.d(TAG, "http url=" + urlBuilder.toString());
			HttpGet getMethod = new HttpGet(urlBuilder.toString());
			HttpResponse response = client.execute(getMethod);
			Log.d(TAG, "http response code="
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				String tmp = read(response);
				Log.d(TAG, "http response=" + tmp);
				return tmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String read(HttpResponse response) {
		String result = null;
		InputStream inputStream = null;
		HttpEntity entity = null;
		try {
			entity = response.getEntity();
			inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			Header header = response.getFirstHeader("Content-Encoding");
			if (header != null
					&& header.getValue().toLowerCase().indexOf("gzip") > -1) {
				inputStream = new GZIPInputStream(inputStream);
			}

			int readBytes = 0;
			byte[] sBuffer = new byte[1024 * 4];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}

			result = new String(content.toByteArray());
			return result;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * post
	 * 
	 * @param urlString
	 * @param params
	 * @return
	 */
	public static String post(Context context, String urlString,
			List<BasicNameValuePair> params) {

		String urlAndParams = ":url=" + urlString + ",params="
				+ (null == params ? "无参数" : params.toString() + ",result=");
		Log.w("trainpost", "00:" + urlAndParams);
		try {
			HttpClient client = getNewHttpClient(context);
			HttpPost postMethod = new HttpPost(urlString);
			if (null != params) {
				addSecureParams(params);
				postMethod.setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			}
			HttpResponse response = client.execute(postMethod);
			int statueCode = response.getStatusLine().getStatusCode();
			if (statueCode == 200) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.w("trainpost", "01:" + urlAndParams + result);
				return result;
			}
			Log.w("trainpost", "02:" + "return null " + urlAndParams
					+ EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			String re = "{\"code\":\"1\",\"msg\":\"未知异常 \"}";
			Log.w("trainpost", "03:" + urlAndParams + re + "," + e.getMessage());
		}
		return null;
	}

	public static long expires(String second) {
		Long l = Long.valueOf(second);
		return l * 1000L + System.currentTimeMillis();
	}

	public static HttpClient getNewHttpClient(Context context) throws Exception {
		// try {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);

		SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 20000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sf, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);

		String proxyAddress = null;
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			String mCurrentApnName = null;
			mCurrentApnName = getCurrentApnInUse(context);
			if (mCurrentApnName.startsWith("cmwp")
					|| mCurrentApnName.startsWith("uniwap")
					|| mCurrentApnName.startsWith("g3wap")) {
				proxyAddress = "10.0.0.172";
			} else if (mCurrentApnName.startsWith("ctwap")) {
				proxyAddress = "10.0.0.200";
			} else {
				return client;
			}
			HttpHost proxy = new HttpHost(proxyAddress, 80);
			client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					proxy);
		}
		return client;
		// } catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }
	}

	public static String getCurrentApnInUse(Context mcontext) {
		String name = "no";
		ConnectivityManager manager = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
			if (activeNetInfo != null && activeNetInfo.isAvailable()) {
				name = activeNetInfo.getExtraInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private static void addSecureParams(Map<String, String> params) {
		// TODO
	}

	private static void addSecureParams(List<BasicNameValuePair> params) {
		// TODO
	}

	/**
	 * 从网络获取图片
	 * 
	 * @param 图片路径bitmapPath
	 * @param 超时时间timeout
	 * @return 返回bitmap
	 * @throws 网络错误或者解析图片为null的时候抛出Exception
	 */
	public static Bitmap getHttpBitmap(String bitmapPath, int timeout, int width)
			throws Exception {
		// long time = System.currentTimeMillis();
		// 获取缓存
		Bitmap bitmap = ImageCacheUtils.getBitmapFormCache(bitmapPath, width);
		if (bitmap != null)
			return bitmap;
		InputStream is = null;
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = new URL(bitmapPath);
		conn = (HttpURLConnection) url.openConnection();
		if (timeout > 0) {
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
		}
		conn.setRequestProperty("Connection", "close");
		conn.connect();
		Log.d("shit",
				"pic theoretically length==================="
						+ conn.getContentLength() + "; bitmapPath:"
						+ bitmapPath);
		is = conn.getInputStream();
		url = null;
		if (conn.getResponseCode() == HTTP_STATE_OK) {
			bis = new BufferedInputStream(is, BUFFER_SIZE);
			int i = -1;
			byte buf[] = new byte[4 * 1024];
			while ((i = bis.read(buf)) != -1) {
				out.write(buf, 0, i);
			}
			byte imgData[] = out.toByteArray();
			Log.d("shit", "pic actual length==================="
					+ imgData.length + "bitmapPath:" + bitmapPath);
			if (conn.getContentLength() == imgData.length) {
				Log.d("pic", "size correct");
				bitmap = ImageUtil.zoomByWidth(imgData, width);
			} else {
				bitmap = null;
			}

		}
		try {
			if (is != null)
				is.close();
			if (bis != null)
				bis.close();
			if (out != null)
				out.close();
			if (conn != null)
				conn.disconnect();
			is = null;
			bis = null;
			out = null;
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bitmap == null) {
			throw new Exception();
		} else {
			// 图片本地存储
			ImageCacheUtils.saveBitmapToCache(bitmapPath, bitmap);
			return bitmap;
		}
	}

	/**
	 * 默认超时时间读取图片的方法
	 * 
	 * @param bitmapPath
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getHttpBitmap(String bitmapPath, int width)
			throws Exception {
		return getHttpBitmap(bitmapPath, DEFAULT_BITMAP_TIMEOUT, width);
	}
}
