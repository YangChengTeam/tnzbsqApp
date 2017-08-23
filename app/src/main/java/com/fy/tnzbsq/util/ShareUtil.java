package com.fy.tnzbsq.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.view.CustomDialog;
import com.kk.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangkai on 16/10/21.
 */
public class ShareUtil {

	public static void openWXShareWithImage(final Context ctx, final String content, final List<String> images,
			final int type) {

		if (!CheckUtil.isWeixinAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装微信");
			return;
		}

		final CustomDialog shareDialog = new CustomDialog(ctx);
		shareDialog.setTitle("正在分享");
		shareDialog.show();

		if (mOkHttpClient == null) {
			mOkHttpClient = new OkHttpClient();
		}

		new AsyncTask<Void, Void, ArrayList<Uri>>() {
			@Override
			protected ArrayList<Uri> doInBackground(Void... arg0) {

				ArrayList<Uri> list = new ArrayList<Uri>();
				if (images == null) {
					return list;
				}

				for (String imgUrl : images) {
					try {

						Request request = new Request.Builder().url(imgUrl).build();
						Response response = mOkHttpClient.newCall(request).execute();
						InputStream is = response.body().byteStream();
						Bitmap bm = BitmapFactory.decodeStream(is);
						list.add(getImageUri(ctx, bm));

					} catch (Exception e) {
					}
				}
				return list;
			}

			protected void onPostExecute(ArrayList<Uri> uris) {
				String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
				if (type == 1) {
					activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
				}
				Intent intent = new Intent(Intent.ACTION_SEND); // 地址
				ComponentName componentName = new ComponentName("com.tencent.mm", activityName);
				intent.setComponent(componentName);
				if (uris.size() > 0) {
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_SEND_MULTIPLE);
					intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
				}
				intent.putExtra("Kdescription", content);
				ctx.startActivity(Intent.createChooser(intent, "分享"));
				shareDialog.dismiss();
			}

		}.execute();
	}

	public static void openWXShareWithImage(final Context ctx, final String content, final String imageUrl,
			final int type) {
		if (!CheckUtil.isWeixinAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装微信");
			return;
		}

		if (mOkHttpClient == null) {
			mOkHttpClient = new OkHttpClient();
		}

		final CustomDialog shareDialog = new CustomDialog(ctx);
		shareDialog.setTitle("正在分享");
		shareDialog.show();

		Request request = new Request.Builder().url(imageUrl).build();
		Call call = mOkHttpClient.newCall(request);

		// 请求加入队列
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				// 此处处理请求失败的业务逻辑
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				shareDialog.dismiss();

				// response的body是图片的byte字节
				byte[] bytes = response.body().bytes();
				// response.body().close();

				// 把byte字节组装成图片
				Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				Uri uri = getImageUri(ctx, bmp);

				if (uri == null)
					return;
				String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
				if (type == 1) {
					activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
				}
				Intent intent = new Intent(Intent.ACTION_SEND); // 地址
				ComponentName componentName = new ComponentName("com.tencent.mm", activityName);
				intent.setComponent(componentName);
				intent.putExtra(Intent.EXTRA_STREAM, uri);
				intent.putExtra("Kdescription", content);
				intent.setType("image/*");
				ctx.startActivity(Intent.createChooser(intent, "分享"));
			}
		});

	}

	public static void openWXShareWithImage(final Context ctx, final String content,Bitmap sBitMap) {
		CheckUtil.setPackageNames(ctx);
		if (!CheckUtil.isWeixinAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装微信");
			return;
		}

		if(sBitMap == null){
			sBitMap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo);
		}

		Uri uri = getImageUri(ctx, sBitMap);

		if (uri == null)
			return;

		Intent intent = new Intent();
		ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
		intent.setComponent(componentName);
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		//intent.putExtra(Intent.EXTRA_TEXT, "测试微信");
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		ctx.startActivity(Intent.createChooser(intent, "分享"));

	}


	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public static void OpenWxShareText(final Context ctx, String content) {
		if (!CheckUtil.isWeixinAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装微信");
			return;
		}
		Intent intent = new Intent("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
		ctx.startActivity(intent);
	}

	public static OkHttpClient mOkHttpClient;

	/**
	 * 加载活动图片
	 * 
	 * @param 'imageUrl'
	 * @param 'moduleIv'
	 */
	public static List<Uri> loadImageUri(final Context context, final List<String> images) {

		final List<Uri> uriList = new ArrayList<Uri>();

		if (mOkHttpClient == null) {
			mOkHttpClient = new OkHttpClient();
		}

		for (String imgUrl : images) {
			// 创建OkHttpClient针对某个imageUrl的数据请求
			Request request = new Request.Builder().url(imgUrl).build();
			Call call = mOkHttpClient.newCall(request);

			// 请求加入队列
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					// 此处处理请求失败的业务逻辑
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					// response的body是图片的byte字节
					byte[] bytes = response.body().bytes();
					// response.body().close();

					// 把byte字节组装成图片
					Bitmap tempBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					uriList.add(getImageUri(context, tempBitmap));
				}
			});
		}

		return uriList;
	}

	public static void openQQShareWithText(final Context ctx, final String content) {

		if (!CheckUtil.isQQAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装QQ");
			return;
		}

		String activityName = "com.tencent.mobileqq.activity.JumpActivity";

		Intent intent = new Intent(Intent.ACTION_SEND); // 地址
		ComponentName componentName = new ComponentName("com.tencent.mobileqq", activityName);
		intent.setComponent(componentName);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "游戏SDK分享");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		ctx.startActivity(intent);
	}

	public static void openWeiboShareWithImage(final Context ctx, final String content) {

		if (!CheckUtil.isWeiboAvilible(ctx)) {
			ToastUtil.toast(ctx, "请安装微博");
			return;
		}
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");

		PackageManager pm = ctx.getPackageManager();
		List<ResolveInfo> matches = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		String packageName = "com.sina.weibo";
		ResolveInfo info = null;

		for (ResolveInfo each : matches) {
			String pkgName = each.activityInfo.applicationInfo.packageName;
			if (packageName.equals(pkgName)) {
				info = each;
				break;
			}
		}

		intent.setClassName(packageName, info.activityInfo.name);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		ctx.startActivity(intent);
	}

}
