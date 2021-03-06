package com.fy.tnzbsq.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.CreateBeforeActivity;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.CheckUtil;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.pay.other.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.utils.SystemTool;

public class ChargeDialog extends Dialog implements WeiXinFollowDialog.StartTimeListener {

    private Context context;

    private Dialog dialog;

    private IPayAbs iPayAbs;

    private String goodId;

    String payWayName = "wxpay";

    RadioGroup payRadioGroup;

    public interface TimeListener {
        void timeStart();
    }

    public TimeListener timeListener;

    public void setTimeListener(TimeListener timeListener) {
        this.timeListener = timeListener;
    }


    public ChargeDialog(Context context, String goodId) {
        super(context, R.style.Dialog);
        iPayAbs = new I1PayAbs((Activity) context);
        this.context = context;
        this.goodId = goodId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void startTimer() {
        if (timeListener != null) {
            timeListener.timeStart();
        }
    }

    public void init() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.charge_dialog, null);


        payRadioGroup = (RadioGroup) view.findViewById(R.id.pay_group);

        TextView singleTv = (TextView) view.findViewById(R.id.tv_single);
        TextView singleHintTv = (TextView) view.findViewById(R.id.tv_single_hint);
        TextView vipTv = (TextView) view.findViewById(R.id.tv_vip);
        TextView vipHintTv = (TextView) view.findViewById(R.id.tv_vip_hint);
        LinearLayout marketLayout = (LinearLayout) view.findViewById(R.id.layout_goto_market);
        setContentView(view);

        if (!StringUtils.isEmpty(App.sigleRemark)) {
            String[] singles = App.sigleRemark.split("&");
            singleTv.setText(singles[0]);
            singleHintTv.setText(singles[1]);
        }
        if (!StringUtils.isEmpty(App.vipRemark)) {
            String[] vips = App.vipRemark.split("&");
            vipTv.setText(vips[0]);
            vipHintTv.setText(vips[1]);
        }

        LinearLayout oneLayout = (LinearLayout) view.findViewById(R.id.layout_one_charge);
        LinearLayout vipLayout = (LinearLayout) view.findViewById(R.id.layout_vip_charge);
        oneLayout.setOnClickListener(new clickListener());
        vipLayout.setOnClickListener(new clickListener());
        marketLayout.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.7
        dialogWindow.setAttributes(lp);

        boolean isComment = PreferencesUtils.getBoolean(context, "is_comment", false);
        if (isComment) {
            marketLayout.setVisibility(View.GONE);
        } else {
            marketLayout.setVisibility(View.VISIBLE);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.layout_one_charge:
                    // 自定义事件,统计次数
                    MobclickAgent.onEvent(context, "buy_single_click", SystemTool.getAppVersionName(context));
                    buy(1, App.siglePrice == 0 ? 2f : App.siglePrice, "装逼神器素材");
                    break;
                case R.id.layout_vip_charge:
                    MobclickAgent.onEvent(context, "buy_vip_click", SystemTool.getAppVersionName(context));
                    buy(2, App.vipPrice == 0 ? 18f : App.vipPrice, "装逼神器VIP");
                    break;
                case R.id.layout_goto_market:

                    if (!CheckUtil.isWxInstall(context)) {
                        ToastUtil.toast(context, "请安装微信");
                        return;
                    }

                    if(StringUtils.isEmpty(App.weixinUrl)){
                        return;
                    }

                    if (App.weixinState == 1) {
                        WebPopupWindow webPopupWindow = new WebPopupWindow((CreateBeforeActivity)context, App.weixinUrl);
                        webPopupWindow.show(((CreateBeforeActivity)context).getWindow().getDecorView().getRootView());
                        startTimer();
                        closeChargeDialog();
                        return;
                    }else {
                        MobclickAgent.onEvent(context, "weixin_click", SystemTool.getAppVersionName(context));
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setPrimaryClip(ClipData.newPlainText(null, "腾牛装逼神器"));
                        ToastUtil.toast(context, "复制成功，可以关注公众号了");

                        WeiXinFollowDialog weiXinFollowDialog = new WeiXinFollowDialog(context);
                        weiXinFollowDialog.setStartTimeListener(ChargeDialog.this);
                        weiXinFollowDialog.showChargeDialog(weiXinFollowDialog);
                        closeChargeDialog();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    public void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            timeListener.timeStart();
            ((Activity) context).startActivityForResult(goToMarket, 1);
            dismiss();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "手机上未安装应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void buy(final int type, float price, String title) {
        String user_id = App.loginUser != null ? App.loginUser.id + "" : "";
        OrderParamsInfo orderParamsInfo = new OrderParamsInfo(Server.URL_PAY, goodId, type + "", price, title, user_id);

        for (int i = 0; i < payRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) payRadioGroup.getChildAt(i);
            if (radioButton.isChecked()) {
                if (radioButton.getId() == R.id.alipay_btn) {
                    payWayName = "alipay";
                }
                if (radioButton.getId() == R.id.wx_btn) {
                    payWayName = "wxpay";
                }
            }
        }

        orderParamsInfo.setPayway_name(payWayName);

        iPayAbs.pay(orderParamsInfo, new IPayCallback() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                //Toast.makeText(context, "购买成功", Toast.LENGTH_LONG).show();

                if (type == 1) {

                    String sourceIdsKey = App.loginUser != null ? App.loginUser.id + "_ids" : App.ANDROID_ID + "_ids";

                    StringBuffer sourceIds = new StringBuffer(PreferencesUtils.getString(context, sourceIdsKey, ""));
                    if (!StringUtils.isEmpty(sourceIds.toString())) {
                        sourceIds.append(",");
                    }
                    sourceIds.append(goodId);
                    PreferencesUtils.putString(context, sourceIdsKey, sourceIds.toString());
                }

                if (type == 2) {
                    if (App.loginUser != null) {
                        App.loginUser.is_vip = 1;
                        PreferencesUtils.putObject(context, "login_user", App.loginUser);
                    }
                }

                dismiss();
            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                //Toast.makeText(context, "购买失败", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }

    public void showChargeDialog(Dialog dia) {
        this.dialog = dia;
        this.dialog.show();
    }

    public void closeChargeDialog() {
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