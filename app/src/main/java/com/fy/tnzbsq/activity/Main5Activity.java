package com.fy.tnzbsq.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.MyViewPagerAdapter;
import com.fy.tnzbsq.bean.PriceRet;
import com.fy.tnzbsq.bean.WeiXinInfoRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.util.WeiXinUtil;
import com.fy.tnzbsq.view.CreatePopupWindow;
import com.fy.tnzbsq.view.SpecialNoTitleTab;
import com.fy.tnzbsq.view.SpecialTab;
import com.fy.tnzbsq.view.SpecialTabRound;
import com.fy.tnzbsq.view.WebPopupWindow;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by admin on 2017/9/11.
 */

@RuntimePermissions
public class Main5Activity extends BaseAppActivity implements SpecialNoTitleTab.MainAddListener {

    // 图片选择弹出窗口
    private CreatePopupWindow createWindow;

    private long clickTime = 0;

    OKHttpRequest okHttpRequest = null;

    @BindView(R.id.tv_tips)
    TextView tipsTextView;

    //public String alipyCode = "鹤创依融先星bZ育香";

    public String weixinUrl = "";

    public int weixinState;

    public static Main5Activity mainActivity;

    private AlertDialog alertDialog;

    public static Main5Activity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVars() {
    }

    @Override
    protected void initViews() {
        Main5ActivityPermissionsDispatcher.showRecordWithCheck(this);

        mainActivity = this;
        okHttpRequest = new OKHttpRequest();

        PageNavigationView tab = (PageNavigationView) findViewById(R.id.tab);

        SpecialNoTitleTab noTitleItem = (SpecialNoTitleTab) NoTitleItem(R.mipmap.add_icon, R.mipmap.close_icon, "");
        noTitleItem.setListener(this);

        NavigationController navigationController = tab.custom()
                .addItem(newItem(R.mipmap.main_acts_normal, R.mipmap.main_acts_selected, "首页"))
                .addItem(newItem(R.mipmap.main_fight_normal, R.mipmap.main_fight_selected, "斗图"))
                .addItem(noTitleItem)
                .addItem(newItem(R.mipmap.main_note_normal, R.mipmap.main_note_selected, "广场"))
                .addItem(newItem(R.mipmap.main_my_normal, R.mipmap.main_my_selected, "我的"))
                .build();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), navigationController.getItemCount()));

        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {

            }

            @Override
            public void onRepeat(int index) {

            }
        });

        boolean isShow = PreferencesUtils.getBoolean(context, "tips", true);
        if (!isShow) {
            tipsTextView.setVisibility(View.GONE);
        } else {
            tipsTextView.setVisibility(View.VISIBLE);
        }

        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsTextView.setVisibility(View.GONE);
                PreferencesUtils.putBoolean(context, "tips", false);
            }
        });

        getPriceConfig();
        getWeixinInfo();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Main5ActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRecord() {
        //ToastUtils.showLong("允许使用存储权限");
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void onRecordDenied() {
        Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForRecord(PermissionRequest request) {
        showRationaleDialog(R.string.permission_storage_rationale, request);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void onStorageNeverAskAgain() {
        Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void addListener() {

        tipsTextView.setVisibility(View.GONE);
        PreferencesUtils.putBoolean(context, "tips", false);

        createWindow = new CreatePopupWindow(context, itemsOnClick);
        createWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(context));
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_layout:
                    if (createWindow != null && createWindow.isShowing()) {
                        createWindow.closePopwindow();
                    }
                    break;
                case R.id.create_image_layout:
                    Intent intent = new Intent(context, ImageDiyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.create_word_layout:
                    Intent intentWord = new Intent(context, CreateCardActivity.class);
                    startActivity(intentWord);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF767676);
        mainTab.setTextCheckedColor(0xFFFE5000);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF767676);
        mainTab.setTextCheckedColor(0xFFFE5000);
        return mainTab;
    }

    /**
     * 无标题
     */
    private BaseTabItem NoTitleItem(int drawable, int checkedDrawable, String text) {
        SpecialNoTitleTab mainTab = new SpecialNoTitleTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        return mainTab;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {

        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            System.exit(0);
        }
    }

    public void getWeixinInfo() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("app_name", "zbsq");
        okHttpRequest.aget("http://nz.qqtn.com/zbsq/index.php?m=Home&c=zbsq&a=wx", params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (!StringUtils.isEmpty(response)) {
                    try {
                        WeiXinInfoRet weiXinInfoRet = Contants.gson.fromJson(response, WeiXinInfoRet.class);
                        if (weiXinInfoRet != null && weiXinInfoRet.errCode == 1) {
                            weixinUrl = weiXinInfoRet.data.url;
                            weixinState = weiXinInfoRet.data.status;
                            App.weixinUrl = weiXinInfoRet.data.url;
                            App.weixinState = weiXinInfoRet.data.status;
                        }
                    } catch (Exception e) {
                        Logger.e("getWeixinInfo error --->");
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onBefore() {

            }
        });
    }


    public void getPriceConfig() {

        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        params.put("mime", App.ANDROID_ID);

        kjh.post(Server.URL_PRICE, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] bt) {
                super.onSuccess(headers, bt);

                if (bt != null && bt.length > 0) {
                    String resultValue = new String(bt);
                    if (resultValue != null) {
                        try {
                            PriceRet result = Contants.gson.fromJson(resultValue, new TypeToken<PriceRet>() {
                            }.getType());
                            if (result != null && result.errCode.equals("0")) {
                                App.siglePrice = result.data != null ? Float.parseFloat(result.data.single) : 2f;
                                App.vipPrice = result.data != null ? Float.parseFloat(result.data.vip) : 18f;

                                if (result.data != null && !StringUtils.isEmpty(result.data.singledesp)) {
                                    App.sigleRemark = result.data.singledesp;
                                } else {
                                    App.sigleRemark = "付费解锁&(购买单个素材2元/个)";
                                }

                                if (result.data != null && !StringUtils.isEmpty(result.data.vipdesp)) {
                                    App.vipRemark = result.data.vipdesp;
                                } else {
                                    App.vipRemark = "永久VIP会员&所有素材免费,原价58元现价18.8元";
                                }

                                return;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    public void fixOpenwx() {
        if (StringUtils.isEmpty(weixinUrl)) {
            return;
        }

        if (weixinState == 1) {
            WebPopupWindow webPopupWindow = new WebPopupWindow(this, weixinUrl);
            webPopupWindow.show(getWindow().getDecorView().getRootView());
            return;
        }

        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setMessage("关注【腾牛装逼神器】微信公众号，来炫酷一把吧!");
        build.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setPrimaryClip(ClipData.newPlainText(null, "tnzbsq"));
                        WeiXinUtil.gotoWeiXin(Main5Activity.this, "公众号已复制,正在前往微信...");
                    }
                });
        build.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
        alertDialog = build.create();
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
