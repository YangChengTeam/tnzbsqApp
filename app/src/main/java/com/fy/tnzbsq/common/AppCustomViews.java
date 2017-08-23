package com.fy.tnzbsq.common;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.view.CustomDialogBase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppCustomViews {
	private Context context;

	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private AlertDialog infoDialog;
	private Toast customToast;
	private View customToastLayout;
	private CustomDialogBase copyDialog;
	private TextView copyTv;
	
	public AppCustomViews(Context context) {
		this.context = context;
	}
	
	public interface onAlertDialogBtnClickListener {
		public void onOk();

		public void onCancle();
	}

	public void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
		}
		if (msg == null) {
			msg = "";
		}
		progressDialog.setMessage(msg);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void showAlertDialog(String title, String msg,
			final onAlertDialogBtnClickListener clickListener) {
		if (msg == null) {
			msg = "";
		}
		if (title == null) {
			title = "";
		}
		if (alertDialog == null) {
			/*AlertDialog.Builder build = new AlertDialog.Builder(context);
			build.setMessage(msg);
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickListener.onOk();
						}
					});
			build.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickListener.onCancle();
						}
					});
			alertDialog = build.create();*/
			
			alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle(title);
			alertDialog.show();
			Window window = alertDialog.getWindow();  
			window.setContentView(R.layout.alert_dialog_custom);  
			TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
			tv_title.setText("发现新版本");
			TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);  
			tv_message.setText(msg);
			TextView confirmBtn = (TextView)window.findViewById(R.id.confirm_btn);
			confirmBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clickListener.onOk();
				}
			});
			TextView cancelBtn = (TextView)window.findViewById(R.id.cancel_btn);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clickListener.onCancle();
				}
			});
		}else{
			alertDialog.setTitle(title);
			alertDialog.show();
		}
		//alertDialog.setTitle(title);
		//alertDialog.show();
	}
	
	public void showPositiveOnlyDialog(String title, String msg,
			final onAlertDialogBtnClickListener clickListener, OnKeyListener keylistener) {
		if (msg == null) {
			msg = "";
		}
		if (title == null) {
			title = "";
		}
		if (alertDialog == null) {
			AlertDialog.Builder build = new AlertDialog.Builder(context);
			build.setMessage(msg);
			build.setOnKeyListener(keylistener);
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickListener.onOk();
						}
					});
			alertDialog = build.create();
		}
		alertDialog.setTitle(title);
		alertDialog.show();
	}

	public void hideAlertDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
	}

	public void showInfoDialog(String title, String msg) {
		if (msg == null) {
			msg = "";
		}
		if (title == null) {
			title = "";
		}
		if (infoDialog == null) {
			AlertDialog.Builder build = new AlertDialog.Builder(context);
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							infoDialog.dismiss();
						}
					});
			infoDialog = build.create();
		}
		infoDialog.setTitle(title);
		infoDialog.setMessage(msg);
		infoDialog.show();
	}


	public void showCustomToast(int msgStrId) {
		String msg = context.getResources().getString(msgStrId);
	}

	public void clear() {
		context = null;
		progressDialog = null;
		alertDialog = null;
		infoDialog = null;
	}
}
