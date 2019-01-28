package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.common.Server;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.util.Map;

import rx.Subscriber;

public class UpdateNameDialog extends Dialog {

	public interface UserNameListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshUserNameUI(String userName);
	}

	private UserNameListener listener;

	private Context context;

	private Dialog dialog;

	private String userName;

	private EditText user_name_ev;

	public UpdateNameDialog(Context context, String userName, UserNameListener listener) {
		super(context, R.style.Dialog);
		this.context = context;
		this.userName = userName;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.update_name_dialog, null);
		setContentView(view);

		user_name_ev = (EditText) view.findViewById(R.id.user_name_ev);
		TextView cancelTv = (TextView) view.findViewById(R.id.cancel_tv);
		TextView confirm_tv = (TextView) view.findViewById(R.id.confirm_tv);
		if (userName != null && userName.length() > 0) {
			user_name_ev.setText(userName);
			user_name_ev.setSelection(userName.length());
		}
		cancelTv.setOnClickListener(new clickListener());
		confirm_tv.setOnClickListener(new clickListener());
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.85); // 宽度设置为屏幕的0.85
		dialogWindow.setAttributes(lp);
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancel_tv:
				closeShareDialog();
				break;
			case R.id.confirm_tv:
				if (user_name_ev.getText() == null || user_name_ev.getText().toString().length() == 0) {
					Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}
				updateUserInfo(user_name_ev.getText().toString(), null, null);
				break;
			default:
				break;
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

	/**
	 * 修改用户信息
	 * 
	 * @param userName
	 * @param qqNumber
	 * @param imgPatth
	 */
	public void updateUserInfo(String userName, String qqNumber, String imgPath) {
		KJHttp kjh = new KJHttp();
		HttpParams params = new HttpParams();

		params.put("mime", App.ANDROID_ID);

		if (userName != null && userName.length() > 0) {
			params.put("name", userName);
		}

		if (qqNumber != null && qqNumber.length() > 0) {
			params.put("qq", qqNumber);
		}

		if (imgPath != null && imgPath.length() > 0) {
			params.put("avatar", new File(imgPath));
		}

		params.putHeaders("Cookie", "cookie_tnzbsq");
		kjh.post(Server.URL_UPDATE_USER, params, new HttpCallBack() {
			@Override
			public void onSuccess(Map<String, String> headers, byte[] t) {
				super.onSuccess(t);
				if (t != null && t.length > 0) {
					// String result = new String(t);
					/*
					 * UserRet userRet = Contants.gson.fromJson(result, new
					 * TypeToken<UserRet>() { }.getType()); if
					 * (Result.checkResult(UserInfoActivity.this, userRet)) {
					 * Toast.makeText(UserInfoActivity.this, "修改成功",
					 * Toast.LENGTH_SHORT).show(); }
					 */
					closeShareDialog();
					Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
					listener.refreshUserNameUI(user_name_ev.getText().toString());
				} else {
					Toast.makeText(context, "修改用户信息失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}