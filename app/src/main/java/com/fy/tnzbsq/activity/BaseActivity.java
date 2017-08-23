package com.fy.tnzbsq.activity;

import org.kymjs.kjframe.KJActivity;

import com.fy.tnzbsq.common.AppCustomViews;
import com.fy.tnzbsq.manager.SimpleAsyTaskManager;

import android.content.Context;

public abstract class BaseActivity extends KJActivity {
	protected Context context;
	public SimpleAsyTaskManager taskManager;
	public AppCustomViews customWidgets;
	public void initData() {
		super.initData();
		context = this;
		taskManager = SimpleAsyTaskManager.getInstance();
		customWidgets = new AppCustomViews(context);
	}

	@Override
	protected void onDestroy() {
		taskManager.cancelAllTask();
		super.onDestroy();
	}

}
