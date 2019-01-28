package com.fy.tnzbsq.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.bean.VersionUpdateServiceRet;
import com.fy.tnzbsq.common.AppCustomViews;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.common.StatusCode;
import com.fy.tnzbsq.service.UpdateService;
import com.fy.tnzbsq.util.AlertUtil;
import com.fy.tnzbsq.util.CheckUtil;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.GlideRoundTransform;
import com.fy.tnzbsq.view.PicPopupWindow;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.other.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.fy.tnzbsq.util.Utils.isValidContext;

/**
 * Created by admin on 2017/9/12.
 */

public class MyInfoFragment extends CustomBaseFragment {

    //默认用户名
    String tempUserName = App.ANDROID_ID;

    private Context mContext;

    @BindView(R.id.user_info_layout)
    FrameLayout mUserInfoLayout;

    @BindView(R.id.left_user_icon)
    ImageView mUserImageView;

    @BindView(R.id.iv_is_vip)
    ImageView vipImageView;

    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.logout_layout)
    RelativeLayout mLoginoutLayout;

    @BindView(R.id.login_out_line)
    View lineView;

    @BindView(R.id.login_out_tv)
    TextView loginOutTv;

    @BindView(R.id.order_layout)
    RelativeLayout mOrderLayout;

    @BindView(R.id.send_layout)
    RelativeLayout mSendLayout;

    @BindView(R.id.clear_layout)
    RelativeLayout mClearLayout;

    @BindView(R.id.version_layout)
    RelativeLayout mVersionLayout;

    @BindView(R.id.share_layout)
    RelativeLayout mShareLayout;

    @BindView(R.id.weixin_layout)
    RelativeLayout mWeixinLayout;

    @BindView(R.id.about_layout)
    RelativeLayout mAboutLayout;

    //分享弹出窗口
    private SharePopupWindow shareWindow;

    //图片选择弹出窗口
    private PicPopupWindow picWindow;

    private UMImage image;

    private ProgressDialog dialog;

    private View mRootView;

    public AppCustomViews customWidgets;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Toast.makeText(getActivity(), "缓存已清除", Toast.LENGTH_SHORT).show();
                    // 最后通知更新
                    getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Contants.BASE_SD_DIR)));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.my_fragment, null);
            ButterKnife.bind(this, mRootView);
            init();
        }
        return mRootView;
    }

    public void init() {
        image = new UMImage(mContext, R.mipmap.logo_share);
        customWidgets = new AppCustomViews(mContext);

        //订单记录
        RxView.clicks(mOrderLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent3 = new Intent(mContext, OrderInfoActivity.class);
                startActivity(intent3);
            }
        });
        //投稿
        RxView.clicks(mSendLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent3 = new Intent(mContext, SendActivity.class);
                startActivity(intent3);
            }
        });

        //清除缓存
        RxView.clicks(mClearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                deleteFilesByDirectory(new File(Contants.BASE_IMAGE_DIR));
                deleteFilesByDirectory(new File(Contants.BASE_NORMAL_FILE_DIR));
                deleteFilesByDirectory(new File(Contants.ALL_SMALL_IMAGE_PATH));
                clearCacheFolder(new File(Contants.BASE_SD_DIR), System.currentTimeMillis());
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
        //版本检测
        RxView.clicks(mVersionLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                new VersionUpdateServiceTask(false).execute();
            }
        });
        //分享APP
        RxView.clicks(mShareLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shareWindow = new SharePopupWindow(getActivity(), itemsOnClick);
                shareWindow.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(mContext));
                backgroundAlpha(0.5f);
                shareWindow.setOnDismissListener(new PoponDismissListener());
            }
        });


        RxView.clicks(mWeixinLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!CheckUtil.isWxInstall(getActivity())) {
                    ToastUtil.toast(getActivity(), "请安装微信");
                    return;
                }
                Main5Activity.getMainActivity().fixOpenwx();
            }
        });
        //关于我们
        RxView.clicks(mAboutLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent6 = new Intent(mContext, AboutActivity.class);
                startActivity(intent6);
            }
        });

        //登录
        RxView.clicks(mUserImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (App.loginUser == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        RxView.clicks(mUserInfoLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (App.loginUser == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(userNameTv).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (App.loginUser == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(mLoginoutLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PreferencesUtils.putObject(getActivity(), "login_user", null);
                setUserState();
            }
        });
        //StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(),null);
        //StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.common_color));
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    @Override
    public void onResume() {
        super.onResume();
        setUserState();
    }

    public void setUserState() {
        User user = (User) PreferencesUtils.getObject(mContext, "login_user", User.class);
        App.loginUser = user;
        if (App.loginUser != null) {
            Glide.with(getActivity()).load(user.logo).transform(new GlideRoundTransform(getActivity(), 25)).into(mUserImageView);
            try {
                userNameTv.setText(StringUtils.isEmpty(user.nickname) ? "火星用户" : URLDecoder.decode(user.nickname, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (user.is_vip == 1) {
                vipImageView.setVisibility(View.VISIBLE);
            }
            mLoginoutLayout.setVisibility(View.VISIBLE);
            mOrderLayout.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
        } else {
            vipImageView.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            mLoginoutLayout.setVisibility(View.GONE);
            mOrderLayout.setVisibility(View.GONE);
            userNameTv.setText(getString(R.string.default_user_name_text));
            mUserImageView.setImageResource(R.mipmap.user_default_img);
        }
    }

    private String downUrl = "";
    private String newVersionName = "";
    private String versionRemark = "";
    private int isForce = 0;
    private class VersionUpdateServiceTask extends AsyncTask<Void, Void, VersionUpdateServiceRet> {
        boolean isAutoUpdate = true;

        public VersionUpdateServiceTask(boolean isAutoUpdate) {
            this.isAutoUpdate = isAutoUpdate;
        }

        private AppCustomViews.onAlertDialogBtnClickListener clickListener = new AppCustomViews.onAlertDialogBtnClickListener() {

            @Override
            public void onOk() {
                customWidgets.hideAlertDialog();
                // startActivity(new Intent(mContext,
                // UpdateActivity.class));

                if (downUrl != null && downUrl.length() > 0) {
                    Intent intent = new Intent(mContext, UpdateService.class);
                    intent.putExtra(Contants.COMMON_URL, downUrl);
                    mContext.startService(intent);
                } else {
                    // Message message = new Message();
                    // message.what = 5;
                    // handler.sendMessage(message);
                    // TODO错误时的处理
                    return;
                }
            }

            @Override
            public void onCancle() {
                customWidgets.hideAlertDialog();
                if(isForce == 1) {
                    ((Main5Activity)getActivity()).finish();
                }
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(VersionUpdateServiceRet result) {
            super.onPostExecute(result);
            // customWidgets.hideProgressDialog();
            if (result != null) {
                if (result.errCode.equals(StatusCode.STATUS_CODE_SUCCESS)) {
                    if (result.data.version == null) {
                        if (!isAutoUpdate) {
                            AlertUtil.show(mContext, "没有版本信息!");
                        }
                        return;
                    }

                    if (result.data.version.equals(CommUtils.getVersionName(mContext))) {
                        if (!isAutoUpdate) {
                            customWidgets.showInfoDialog("检查更新",
                                    "当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(mContext));
                        }
                    } else {
                        if (result.data.versionCode != null && Integer.parseInt(result.data.versionCode) > CommUtils.getVersionCode(getActivity())) {
                            if (!CommUtils.checkSdCardExist()) {
                                if (!isAutoUpdate) {
                                    AlertUtil.show(getActivity(), "sd卡无法使用或不存在！请插入sd卡。");
                                }
                            } else {
                                downUrl = result.data.download;
                                newVersionName = result.data.version;
                                versionRemark = result.data.updateLog;
                                isForce = result.data.isForce;

                                if (versionRemark != null) {
                                    versionRemark = versionRemark.replace(",", "\n");
                                }

                                showUpdateDialog();
                            }
                        } else {
                            customWidgets.showInfoDialog("检查更新",
                                    "当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(getActivity()));
                        }
                    }
                } else {
                    // AlertUtil.show(mContext, result.body.remark);
                }
            } else {
                if (isValidContext(mContext)) {
                    AlertUtil.show(mContext, mContext.getResources().getString(R.string.net_connect_error));
                }
            }
        }

        private void showUpdateDialog() {
            if (isResumed()) {
                customWidgets.showAlertDialog(isForce, "检查更新", "当前版本号：V" + CommUtils.getVersionName(getActivity()) + "\n最新版本号：V"
                        + newVersionName + "\n版本信息：\n" + versionRemark + "\n是否下载并安装新版本？", clickListener);
            }
        }

        @Override
        protected VersionUpdateServiceRet doInBackground(Void... params) {
            try {
                return ServiceInterface.VersionUpdateService(mContext, getMetaDataValue("UMENG_CHANNEL"));
            } catch (Exception e) {
            }
            return null;
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    //弹出窗口监听消失
    public class PoponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    public void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    private String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
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
            Toast.makeText(mContext, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(mContext, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(mContext, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void ShareWeb(int thumb_img, SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(mContext, thumb_img);
        UMWeb web = new UMWeb("http://zs.qqtn.com");
        web.setThumb(thumb);
        web.setDescription("2018开启新年装逼新玩法，腾小牛在这里等你来挑战！");
        web.setTitle("装逼神器@你，并向你发起了装逼挑战！");
        new ShareAction((Activity) mContext).withMedia(web).setPlatform(platform).setCallback(umShareListener).share();
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
                    if (picWindow != null && picWindow.isShowing()) {
                        picWindow.dismiss();
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
                case R.id.local_pic_layout:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        HeadImageUtils.cutPhoto = null;
                        HeadImageUtils.imgPath = null;
                        HeadImageUtils.imgResultPath = null;
                        HeadImageUtils.getFromLocation(getActivity());
                        if (picWindow != null && picWindow.isShowing()) {
                            picWindow.dismiss();
                        }
                    } else {
                        Toast.makeText(mContext, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.photo_layout:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        HeadImageUtils.getFromCamara(getActivity());
                        if (picWindow != null && picWindow.isShowing()) {
                            picWindow.dismiss();
                        }
                    } else {
                        Toast.makeText(mContext, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
