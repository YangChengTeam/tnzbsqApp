package com.fy.tnzbsq.util;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

public class TaskUtil {
	public static boolean isTaskFinished(AsyncTask<?, ?, ?> task) {
		if (task != null && task.getStatus() != Status.FINISHED) {
			return false;
		}
		return true;
	}

	public static void cancelTask(AsyncTask<?, ?, ?> task) {
		if (task != null && task.getStatus() != Status.FINISHED) {
			task.cancel(true);
		}
	}
}
