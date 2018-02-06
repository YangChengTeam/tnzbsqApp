package com.fy.tnzbsq.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;


public class AboutActivity extends BaseAppActivity {

    @BindView(R.id.about_layout)
    RelativeLayout aboutLayout;

    @BindView(R.id.back_img)
    ImageView backImg;

    @BindView(R.id.top_title)
    TextView titleNameTv;

    @BindView(R.id.iv_weixin_code)
    ImageView mWeixinImageView;

    @BindView(R.id.tv_weixin_code)
    TextView mWeixinCodeTextView;

    @BindView(R.id.join_tv)
    TextView joinTv;

    @BindView(R.id.copy_tv)
    TextView copyTv;

    @BindView(R.id.tv_app_show)
    TextView mAppShowTextView;

    @BindView(R.id.tv_version)
    TextView mVersionNameTextView;

    private String url = "";

    //分享弹出窗口
    private SharePopupWindow shareWindow;

    CustomProgress dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.about;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        titleNameTv.setText(getResources().getString(R.string.app_name));
        mAppShowTextView.setText("我们是一群热血青年\n" +
                "我们向往未来，向往外面的世界\n" +
                "不是因为炫耀，纯粹只是娱乐大众\n" +
                "这就是我，真真正正的我");

        mVersionNameTextView.setText(CommUtils.getVersionName(AboutActivity.this));

        dialog = CustomProgress.create(AboutActivity.this, "正在分享...", true, null);
        dialog.setTitle("装B神器分享");


        RxView.clicks(backImg).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
               finish();
            }
        });

        RxView.clicks(joinTv).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                joinQQGroup("7BFn1GxAlvk1i9ZxdpEtysy4nDRrszid");
            }
        });

        RxView.clicks(copyTv).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, "3258186647"));
                ToastUtil.toast(AboutActivity.this, "复制成功，可以加QQ了");
            }
        });

        RxView.clicks(mWeixinCodeTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, "tnzbsq"));
                ToastUtil.toast(AboutActivity.this, "复制成功，可以加微信了");
            }
        });

        RxView.clicks(mWeixinImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shareWindow = new SharePopupWindow(AboutActivity.this, itemsOnClick);
                shareWindow.showAtLocation(aboutLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(AboutActivity.this));
                backgroundAlpha(0.5f);
                shareWindow.setOnDismissListener(new PoponDismissListener());
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    /****************
     *
     * 发起添加群流程。群号：装逼神器官方群(479687673) 的 key 为： 7BFn1GxAlvk1i9ZxdpEtysy4nDRrszid
     * 调用 joinQQGroup(7BFn1GxAlvk1i9ZxdpEtysy4nDRrszid) 即可发起手Q客户端申请加群 装逼神器官方群(479687673)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d("plat", "platform" + platform);
            ToastUtil.toast(AboutActivity.this, "分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            ToastUtil.toast(AboutActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            ToastUtil.toast(AboutActivity.this, "分享取消了");
        }
    };

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    //弹出窗口监听消失
    public class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void ShareWeb(int thumb_img, SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(AboutActivity.this, thumb_img);
        UMWeb web = new UMWeb("http://zs.qqtn.com");
        web.setThumb(thumb);
        web.setDescription("2018开启新年装逼新玩法，腾小牛在这里等你来挑战！");
        web.setTitle("装逼神器@你，并向你发起了装逼挑战！");
        new ShareAction(this).withMedia(web).setPlatform(platform).setCallback(umShareListener).share();
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_layout:
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qq_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.QQ);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qzone_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.QZONE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wechat_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.WEIXIN);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wxcircle_layout:
                    ShareWeb(R.mipmap.logo_share, SHARE_MEDIA.WEIXIN_CIRCLE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
