package com.fy.tnzbsq.manager;

import java.util.ArrayList;
import java.util.List;

import com.fy.tnzbsq.util.TaskUtil;

import android.os.AsyncTask;

public class SimpleAsyTaskManager {
	public static final int TASK_TYPE_GET_SHOW_DATA = 0x0;
	public static final int TASK_TYPE_SEND_USER_DATA = 0x1;//暂时未用

	private List<AsyncTask<?, ?, ?>> tasks;
	private List<Integer> taskTypes;
	private static SimpleAsyTaskManager instance;

	private SimpleAsyTaskManager() {
		tasks = new ArrayList<AsyncTask<?, ?, ?>>();
		taskTypes = new ArrayList<Integer>();
	}

	public static SimpleAsyTaskManager getInstance() {
		if (instance == null) {
			instance = new SimpleAsyTaskManager();
		}
		return instance;
	}

	public void addTask(AsyncTask<?, ?, ?> task, int taskType) {
		tasks.add(task);
		taskTypes.add(taskType);
	}

	public void cancelAllTask() {
		for (int i = 0; i < tasks.size(); i++) {
			if (!TaskUtil.isTaskFinished(tasks.get(i))) {
				tasks.get(i).cancel(true);
			}
		}
		tasks.clear();
		taskTypes.clear();
	}

	public void cancelTask(AsyncTask<?, ?, ?> t) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i) == t) {
				tasks.get(i).cancel(true);
				tasks.remove(i);
				taskTypes.remove(i);
			}
		}
	}

	public void cancelTasks(int[] types) {
		for (int i = 0; i < tasks.size(); i++) {
			for (int j = 0; j < types.length; j++) {
				if (taskTypes.get(i) == types[j]) {
					tasks.get(i).cancel(true);
					tasks.remove(i);
					taskTypes.remove(i);
					i--;
				}
			}
		}
	}
}
