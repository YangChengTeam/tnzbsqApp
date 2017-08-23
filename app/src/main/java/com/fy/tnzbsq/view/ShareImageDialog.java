package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

public class ShareImageDialog extends Dialog {

	private Context context;

	private Dialog dialog;

	private UMImage image;

	public ShareImageDialog(Context context, UMImage img) {
		super(context, R.style.Dialog);
		this.context = context;
		this.image = img;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.share_custom1, null);
		setContentView(view);

		RelativeLayout wechatIv = (RelativeLayout) view.findViewById(R.id.wechat_layout);
		RelativeLayout wxcircleIv = (RelativeLayout) view.findViewById(R.id.wxcircle_layout);
		RelativeLayout qqonIv = (RelativeLayout) view.findViewById(R.id.qq_layout);
		RelativeLayout qzoneIv = (RelativeLayout) view.findViewById(R.id.qzone_layout);
		wechatIv.setOnClickListener(new clickListener());
		wxcircleIv.setOnClickListener(new clickListener());
		qqonIv.setOnClickListener(new clickListener());
		qzoneIv.setOnClickListener(new clickListener());

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.65); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	private void ShareWeb(int thumb_img,SHARE_MEDIA platform){
		UMImage thumb = new UMImage(context,thumb_img);
		UMWeb web = new UMWeb("http://zs.qqtn.com");
		web.setThumb(thumb);
		web.setDescription("有人向你发起了装逼挑战，是否一战？");
		web.setTitle("装逼神器");
		new ShareAction((Activity) context).withMedia(web).setPlatform(platform).setCallback(umShareListener).share();
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (image == null) {
				image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.def_logo));
			}

			int id = v.getId();
			switch (id) {
			case R.id.wechat_layout:
				new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
						.withMedia(image).share();
				break;
			case R.id.wxcircle_layout:
				new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
						.withMedia(image).share();
				break;
			case R.id.qq_layout:
				new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
						.withMedia(image).share();
				break;
			case R.id.qzone_layout:
				/*new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
						.withText("装逼神器").withTargetUrl("http://zs.qqtn.com").withMedia(image).share();*/
				break;
			}
		}
	};

	private UMShareListener umShareListener = new UMShareListener() {
		/**
		 * @descrption 分享开始的回调
		 * @param platform 平台类型
		 */
		@Override
		public void onStart(SHARE_MEDIA platform) {
			SocializeUtils.safeShowDialog(dialog);
		}
		@Override
		public void onResult(SHARE_MEDIA platform) {
			try {
				Log.d("plat", "platform" + platform);
				Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
				closeShareDialog();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			try {
				Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
				closeShareDialog();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			try {
				Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
				closeShareDialog();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void showShareDialog(Dialog dia) {
		this.dialog = dia;
		this.dialog.show();
	}

	public void closeShareDialog() {
		if (isValidContext(context) && dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
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
}