package com.fy.tnzbsq.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.BubbleAdapter;
import com.fy.tnzbsq.adapter.TypeFaceAdapter;
import com.fy.tnzbsq.bean.WordCreateRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.ImageUtil;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.HorizontialListView;
import com.fy.tnzbsq.view.ShareFightDialog;
import com.fy.tnzbsq.view.image.GLFont;
import com.fy.tnzbsq.view.image.StickerView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WordDiyActivity extends BaseActivity {

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.paint_layout, click = true)
	private FrameLayout paintLayout;

	@BindView(id = R.id.share_layout, click = true)
	private LinearLayout shareLayout;

	@BindView(id = R.id.word_tip_tv, click = true)
	private TextView wordTipTv;

	@BindView(id = R.id.image_make_diy_input)
	private EditText imageMakeEt;

	@BindView(id = R.id.create_word_btn, click = true)
	private Button wordCreateBtn;

	@BindView(id = R.id.sticker_diy_view, click = true)
	private StickerView stickerView;

	@BindView(id = R.id.type_face_list)
	private HorizontialListView typefaceListView;

	@BindView(id = R.id.create_color_list)
	private HorizontialListView createColorListView;

	@BindView(id = R.id.frame_list)
	private HorizontialListView frameListView;

	private TypeFaceAdapter adapter;

	private TypeFaceAdapter colorAdapter;

	private BubbleAdapter frameAdapter;

	// 底图
	private Bitmap mainBitmap;
	// 绘制的文字标签图
	private Bitmap textBitmap;

	private Bitmap showBitmap;

	private Bitmap tempBitmap;

	private static final int[] typedfaceData = { R.mipmap.typeface_default, R.mipmap.typeface_icon1,
			R.mipmap.typeface_icon2, R.mipmap.typeface_icon3, R.mipmap.typeface_icon4, R.mipmap.typeface_icon5,
			R.mipmap.typeface_icon6, R.mipmap.typeface_icon7, R.mipmap.typeface_icon8, R.mipmap.typeface_icon9,
			R.mipmap.typeface_icon10, R.mipmap.typeface_icon11, R.mipmap.typeface_icon12,
			R.mipmap.typeface_icon13, R.mipmap.typeface_icon14 };

	private static final int[] colorData = { R.mipmap.color_default, R.mipmap.create_color1,
			R.mipmap.create_color2, R.mipmap.create_color3, R.mipmap.create_color4, R.mipmap.create_color5,
			R.mipmap.create_color6, R.mipmap.create_color7, R.mipmap.create_color8 };

	private static final int[] frameData = { R.mipmap.bubble_default, R.mipmap.frame1, R.mipmap.frame2,
			R.mipmap.frame3, R.mipmap.frame4, R.mipmap.frame5, R.mipmap.frame6, R.mipmap.frame7,
			R.mipmap.frame8, R.mipmap.frame9, R.mipmap.frame10 };

	private static final String[] colorDataRGB = { "#000000", "#ff0000", "#ffff00", "#00ff00", "#00ffff", "#00a2e8",
			"#ff00ff", "#ffaec9", "#cccc00" };

	// 使用的字体的编号
	private int typefaceNum;

	private Typeface typeFace;

	// 使用的颜色的编号
	private int colorNum;

	private CustomProgress dialog;

	private String wordImagePath;

	@Override
	public void setRootView() {
		setContentView(R.layout.word_diy);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();

		titleNameTv.setText(getResources().getString(R.string.image_make_text));
		adapter = new TypeFaceAdapter(context, typedfaceData);
		colorAdapter = new TypeFaceAdapter(context, colorData);
		frameAdapter = new BubbleAdapter(context, frameData);

		typefaceListView.setAdapter(adapter);
		createColorListView.setAdapter(colorAdapter);
		frameListView.setAdapter(frameAdapter);

		typeFace = Typeface.create("宋体", Typeface.BOLD);

		// 初始化页面默认绘制
		mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
		/*textBitmap = GLFont.getImage(600, 100, "腾牛装逼神器", 80, DensityUtils.getScreenW(context), typeFace,
				colorDataRGB[colorNum]);
		// 绘制图片
		stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),"腾牛装逼神器");*/

		// 字体改变时，重新绘制图片
		typefaceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (imageMakeEt.getText() != null && imageMakeEt.getText().toString().length() > 0) {
					adapter.setSelectedPosition(position);
					adapter.notifyDataSetChanged();
					typefaceNum = position;
					if (typefaceNum == 0) {
						stickerView.clearBitmap();// 清除画布

						if (wordTipTv.getVisibility() == View.VISIBLE) {
							wordTipTv.setVisibility(View.GONE);
						}

						typeFace = Typeface.create("宋体", Typeface.BOLD);
						textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80,
								DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
						// 合并图片
						stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),"腾牛装逼神器");
					} else {

						String temp = "";
						Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
						Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
						if (m.find()) {
							temp = m.replaceAll("#!");
						} else {
							temp = imageMakeEt.getText().toString();
						}

						WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
								"100", typefaceNum + "",
								(DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 92)) + "");
					}
				}
			}
		});

		// 颜色改变时，重新绘制图片
		createColorListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (imageMakeEt.getText() != null && imageMakeEt.getText().toString().length() > 0) {

					colorAdapter.setSelectedPosition(position);
					colorAdapter.notifyDataSetChanged();
					colorNum = position;

					if (typefaceNum == 0) {
						stickerView.clearBitmap();// 清除画布

						if (wordTipTv.getVisibility() == View.VISIBLE) {
							wordTipTv.setVisibility(View.GONE);
						}

						typeFace = Typeface.create("宋体", Typeface.BOLD);
						textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80,
								DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
						// 合并图片
						stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),"腾牛装逼神器");
					} else {
						String temp = "";
						Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
						Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
						if (m.find()) {
							temp = m.replaceAll("#!");
						} else {
							temp = imageMakeEt.getText().toString();
						}

						WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
								"100", typefaceNum + "",
								(DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 92)) + "");
					}
				}
			}
		});

		// 相框改变时，重新绘制图片
		frameListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (imageMakeEt.getText() != null && imageMakeEt.getText().toString().length() > 0) {
					frameAdapter.setSelectedPosition(position);
					frameAdapter.notifyDataSetChanged();

					mainBitmap = BitmapFactory.decodeResource(getResources(), frameData[position]);

					/*
					 * int DISPLAY_WIDTH = DensityUtils.getScreenW(context) -
					 * 100; int DISPLAY_HEIGHT =
					 * DensityUtils.getScreenH(context) -
					 * DensityUtils.dip2px(context, 285) - 100;
					 * 
					 * double tempPro = (double) mainBitmap.getHeight() /
					 * (double) mainBitmap.getWidth();
					 * 
					 * double newWidth = ((double) DISPLAY_HEIGHT) / tempPro;
					 * 
					 * mainBitmap = ImageUtil.ZoomImg(mainBitmap, (int)
					 * newWidth, DISPLAY_HEIGHT);
					 */

					if (position == 0) {
						mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
					}

					changeBitmap();

					if (typefaceNum == 0) {
						stickerView.clearBitmap();// 清除画布

						if (wordTipTv.getVisibility() == View.VISIBLE) {
							wordTipTv.setVisibility(View.GONE);
						}

						typeFace = Typeface.create("宋体", Typeface.BOLD);
						textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80,
								DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
						// 合并图片
						stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),"腾牛装逼神器");
					} else {
						String temp = "";
						Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
						Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
						if (m.find()) {
							temp = m.replaceAll("#!");
						} else {
							temp = imageMakeEt.getText().toString();
						}

						WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
								"100", typefaceNum + "",
								(DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 92)) + "");
					}
				}
			}
		});

		stickerView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mainBitmap != null && stickerView.getVisibility() == View.VISIBLE) {
					showBitmap = stickerView.saveBitmapToFile();
					if (showBitmap == null) {
						showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
					}

					ShareFightDialog shareFightDialog = new ShareFightDialog(context, showBitmap);
					shareFightDialog.showShareDialog(shareFightDialog);
				}
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.back_img:
			finish();
			break;
		case R.id.share_layout:
			if(mainBitmap != null){
				showBitmap = stickerView.saveBitmapToFile();
				if (showBitmap == null) {
					showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
				}
				ShareFightDialog shareFightDialog = new ShareFightDialog(context, showBitmap);
				shareFightDialog.showShareDialog(shareFightDialog);
			}else{
				Toast.makeText(context, "无法加载图片，请稍后分享", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.create_word_btn:

			if (imageMakeEt.getText() != null && imageMakeEt.getText().toString().length() > 0) {
				if (typefaceNum == 0) {

					stickerView.clearBitmap();// 清除画布

					if (wordTipTv.getVisibility() == View.VISIBLE) {
						wordTipTv.setVisibility(View.GONE);
					}

					typeFace = Typeface.create("宋体", Typeface.BOLD);

					changeBitmap();

					textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80,
							DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
					// 合并图片
					stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),imageMakeEt.getText().toString());
				} else {

					String temp = "";
					Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
					Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
					if (m.find()) {
						temp = m.replaceAll("#!");
					} else {
						temp = imageMakeEt.getText().toString();
					}

					WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp, "100",
							typefaceNum + "",
							(DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 92)) + "");
				}
			}
			break;
		case R.id.word_tip_tv:
			if (wordTipTv.getVisibility() == View.VISIBLE) {
				imageMakeEt.requestFocus();
				imageMakeEt.setFocusable(true);
				InputMethodManager inputManager = (InputMethodManager)imageMakeEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
			           inputManager.showSoftInput(imageMakeEt, 0);
			}
			break;
		default:
			break;
		}
	}

	public int[] getData() {
		for (int i = 0; i < typedfaceData.length; i++) {
			typedfaceData[i] = R.mipmap.typeface_default;
		}
		return typedfaceData;
	}

	/**
	 * 异步加载网络图片并保存到本地
	 *
	 */
	public class WordCreateAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {
		public WordCreateAsyncTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			try {
				byte[] data = readInputStream(getRequest(wordImagePath));
				tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				return tempBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tempBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (isValidContext(context) && dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			if (result != null) {
				textBitmap = result;

				if (mainBitmap == null) {
					mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
				}
				wordTipTv.setVisibility(View.GONE);
				stickerView.clearBitmap();

				// 输入框有文字的时候才绘制文字图片
				if (imageMakeEt.getText().toString().length() > 0) {
					// 绘制图片
					stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(),"腾牛装逼神器");
					result = null;
				}
			}
		}
	}

	/**
	 * 文字处理接口
	 * 
	 * @param color
	 * @param text
	 * @param size
	 * @param font
	 */
	private void WordCreateData(String color, String text, String size, String font, String wordimgwidth) {
		KJHttp kjh = new KJHttp();
		kjh.cleanCache();
		HttpParams params = new HttpParams();
		params.put("mime", App.ANDROID_ID);
		params.put("wcolor", color);
		params.put("wtext", text);
		params.put("wsize", size);
		params.put("wfont", font);
		params.put("wordimgwidth", wordimgwidth);
		kjh.post(Server.URL_WORD_CREATE_DATA, params, new HttpCallBack() {

			@Override
			public void onPreStart() {
				super.onPreStart();

				if (dialog == null) {
					dialog = CustomProgress.create(context, "", true, null);
				}
				dialog.setCanceledOnTouchOutside(false);
				if (isValidContext(context) && dialog != null) {
					dialog.show();
				}
			}

			@Override
			public void onSuccess(Map<String, String> headers, byte[] t) {
				super.onSuccess(headers, t);
				// 获取cookie
				KJLoger.debug("===" + headers.get("Set-Cookie"));

				if (t != null && t.length > 0) {
					String result = new String(t);
					WordCreateRet ret = Contants.gson.fromJson(result, new TypeToken<WordCreateRet>() {
					}.getType());

					if (ret != null) {
						// 当文字处理成功后，从网络加载文字图片
						wordImagePath = ret.data;
						new WordCreateAsyncTask().execute();
					}
				} else {
					Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
					if (isValidContext(context) && dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
				if (isValidContext(context) && dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}

		});
	}

	/**
	 * 文字处理接口
	 * 
	 * @param 'color'
	 * @param 'text'
	 * @param 'size'
	 * @param 'font'
	 */
	private void deleteWordImg(String filename) {
		KJHttp kjh = new KJHttp();
		kjh.cleanCache();
		HttpParams params = new HttpParams();
		params.put("filename", filename);
		kjh.post(Server.URL_WORD_IMG_DELETE_DATA, params, new HttpCallBack() {

			@Override
			public void onPreStart() {
				super.onPreStart();
			}

			@Override
			public void onSuccess(Map<String, String> headers, byte[] t) {
				super.onSuccess(headers, t);
				// 获取cookie
				KJLoger.debug("===" + headers.get("Set-Cookie"));

				if (t != null && t.length > 0) {
					String result = new String(t);
					WordCreateRet ret = Contants.gson.fromJson(result, new TypeToken<WordCreateRet>() {
					}.getType());

					if (ret != null) {
						// 当文字处理成功后，从网络加载文字图片
						wordImagePath = ret.data;
						new WordCreateAsyncTask().execute();
					}
				} else {
					Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
					if (isValidContext(context) && dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
				if (isValidContext(context) && dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}

		});
	}

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(3000); // 5秒
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;

	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4 * 1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	@SuppressLint("NewApi")
	private boolean isValidContext(Context ctx) {
		Activity activity = (Activity) ctx;

		if (Build.VERSION.SDK_INT > 17) {
			if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
				return false;
			} else {
				return true;
			}
		} else {
			if (activity == null || activity.isFinishing()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 改变图片大小
	 */
	public void changeBitmap() {
		int DISPLAY_HEIGHT = DensityUtils.getScreenH(context) - DensityUtils.dip2px(context, 285);

		// 图片的高度/宽度比
		double tempPro = (double) mainBitmap.getHeight() / (double) mainBitmap.getWidth();

		if (tempPro > 1) {
			double newWidth = ((double) DISPLAY_HEIGHT) / tempPro;
			mainBitmap = ImageUtil.ZoomImg(mainBitmap, (int) newWidth, DISPLAY_HEIGHT);
		} else {
			double newHeight = DensityUtils.getScreenW(context) * tempPro;
			mainBitmap = ImageUtil.ZoomImg(mainBitmap, DensityUtils.getScreenW(context), (int) newHeight);
		}

		if (mainBitmap == null) {
			mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mainBitmap != null) {
			mainBitmap.recycle();
			mainBitmap = null;
		}
		if (textBitmap != null) {
			textBitmap.recycle();
			textBitmap = null;
		}
		if (showBitmap != null) {
			showBitmap.recycle();
			showBitmap = null;
		}
		if (tempBitmap != null) {
			tempBitmap.recycle();
			tempBitmap = null;
		}
	}
}
