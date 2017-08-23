package com.fy.tnzbsq.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class MyBroadcastReceiver extends BroadcastReceiver {

    //推送广播
    public static String PUSH_DATA_ACTION = "com.fy.tnzbsq.pushData";

    //解锁屏幕
    public static String USER_PRESENT_ACTION = "android.intent.action.USER_PRESENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("broadcast", "broadcast---action--" + action);
        if (action.equals(PUSH_DATA_ACTION) || action.equals(USER_PRESENT_ACTION)) {
            Log.e("in broadcast", "in broadcast---接收到广播");
            startPush(context);
        }
    }

    /**
     * 启动推送
     */
    public void startPush(Context context) {
        Resources resource = context.getResources();
        //百度推送
        PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, "kNrcQdmhovXlSwSzANMIGs3r");

        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier("notification_custom_builder", "layout", context.getPackageName()),
                resource.getIdentifier("notification_icon", "id", context.getPackageName()),
                resource.getIdentifier("notification_title", "id", context.getPackageName()),
                resource.getIdentifier("notification_text", "id", context.getPackageName()));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "logo_108", "drawable", context.getPackageName()));

        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(context, 1, cBuilder);
    }
}