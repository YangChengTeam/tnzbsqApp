package com.fy.tnzbsq.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.AboutActivity;
import com.fy.tnzbsq.activity.LoginActivity;
import com.fy.tnzbsq.activity.MyCreate1Activity;
import com.fy.tnzbsq.activity.MyKeepActivity;
import com.fy.tnzbsq.activity.SendActivity;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.bean.UserRet;
import com.fy.tnzbsq.bean.VersionUpdateServiceRet;
import com.fy.tnzbsq.common.AppCustomViews;
import com.fy.tnzbsq.common.AppCustomViews.onAlertDialogBtnClickListener;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.common.StatusCode;
import com.fy.tnzbsq.service.UpdateService;
import com.fy.tnzbsq.util.AlertUtil;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.PicPopupWindow;
import com.fy.tnzbsq.view.SharePopupWindow;
import com.fy.tnzbsq.view.UpdateNameDialog;
import com.fy.tnzbsq.view.UpdateNameDialog.UserNameListener;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.widget.RoundImageView;

import java.io.File;
import java.util.Map;

public class LeftMenuFragment extends Fragment implements OnClickListener, UserNameListener {

    // private MainActivity context;

    //默认用户名
    String tempUserName = App.ANDROID_ID;

    private Context context;

    private View view;

    public AppCustomViews customWidgets;

    private RoundImageView userImage;

    private ImageView vipImageView;

    private TextView userNameTv;

    private View lineView;

    private TextView loginOutTv;

    // 版本更新
    // private AsyncTask<?, ?, ?> checkVersionTask;

    //分享弹出窗口
    private SharePopupWindow shareWindow;

    //图片选择弹出窗口
    private PicPopupWindow picWindow;

    private UMImage image;

    private ProgressDialog dialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 2:
                    Toast.makeText(getActivity(), "缓存已清除", Toast.LENGTH_SHORT).show();
                    // 最后通知更新
                    getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Contants.BASE_SD_DIR)));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_left_frag, null);
        context = getActivity();
        image = new UMImage(context, R.mipmap.logo_share);
        customWidgets = new AppCustomViews(context);
        userNameTv = (TextView) view.findViewById(R.id.user_name);
        userImage = (RoundImageView) view.findViewById(R.id.left_user_icon);
        vipImageView = (ImageView) view.findViewById(R.id.iv_is_vip);

        lineView = view.findViewById(R.id.login_out_line);
        loginOutTv =  (TextView) view.findViewById(R.id.login_out_tv);

        userImage.setOnClickListener(this);
        userNameTv.setOnClickListener(this);
        view.findViewById(R.id.my_create_tv).setOnClickListener(this);
        view.findViewById(R.id.my_keep_tv).setOnClickListener(this);
        view.findViewById(R.id.send_tv).setOnClickListener(this);
        view.findViewById(R.id.clear_tv).setOnClickListener(this);
        view.findViewById(R.id.version_tv).setOnClickListener(this);
        view.findViewById(R.id.share_tv).setOnClickListener(this);
        view.findViewById(R.id.about_tv).setOnClickListener(this);
        view.findViewById(R.id.login_out_tv).setOnClickListener(this);
        //getUserInfo();
        dialog = new ProgressDialog(context);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_create_tv:
                Intent intent1 = new Intent(context, MyCreate1Activity.class);
                startActivity(intent1);
                break;
            case R.id.my_keep_tv:
                Intent intent2 = new Intent(context, MyKeepActivity.class);
                startActivity(intent2);
                break;
            case R.id.send_tv:
                Intent intent3 = new Intent(context, SendActivity.class);
                startActivity(intent3);
                break;
            case R.id.clear_tv:
                deleteFilesByDirectory(new File(Contants.BASE_IMAGE_DIR));
                deleteFilesByDirectory(new File(Contants.BASE_NORMAL_FILE_DIR));
                deleteFilesByDirectory(new File(Contants.ALL_SMALL_IMAGE_PATH));
                clearCacheFolder(new File(Contants.BASE_SD_DIR), System.currentTimeMillis());
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                break;
            case R.id.version_tv:
                new VersionUpdateServiceTask(false).execute();
                break;
            case R.id.share_tv:
                //Intent intent5 = new Intent(context, ShareActivity.class);
                //startActivity(intent5);

                shareWindow = new SharePopupWindow(getActivity(), itemsOnClick);
                shareWindow.showAtLocation(getActivity().findViewById(R.id.content_frame), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                backgroundAlpha(0.5f);
                shareWindow.setOnDismissListener(new PoponDismissListener());

                break;
            case R.id.about_tv:
                Intent intent6 = new Intent(context, AboutActivity.class);
                startActivity(intent6);
                break;
            case R.id.left_user_icon:
                if(App.loginUser == null){
                    Intent intent7 = new Intent(context, LoginActivity.class);
                    startActivity(intent7);
                }
                break;
            case R.id.user_name:
                if(App.loginUser == null){
                    Intent intent7 = new Intent(context, LoginActivity.class);
                    startActivity(intent7);
                }else{
                    UpdateNameDialog updateNameDialog = new UpdateNameDialog(context,userNameTv.getText().toString(),this);
                    updateNameDialog.showShareDialog(updateNameDialog);
                }
                break;
            case R.id.login_out_tv:
                PreferencesUtils.putObject(context,"login_user",null);
                setUserState();
                break;
            default:
                break;
        }
    }

    private void ShareWeb(int thumb_img,SHARE_MEDIA platform){
        UMImage thumb = new UMImage(context,thumb_img);
        UMWeb web = new UMWeb("http://zs.qqtn.com");
        web.setThumb(thumb);
        web.setDescription("有人向你发起了装逼挑战，是否一战？");
        web.setTitle("装逼神器");
        new ShareAction((Activity) context).withMedia(web).setPlatform(platform).setCallback(umShareListener).share();
    }

    //为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
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
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.QQ);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.qzone_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.QZONE);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wechat_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.WEIXIN);
                    if (shareWindow != null && shareWindow.isShowing()) {
                        shareWindow.dismiss();
                    }
                    break;
                case R.id.wxcircle_layout:
                    ShareWeb(R.mipmap.logo_share,SHARE_MEDIA.WEIXIN_CIRCLE);
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
                        Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.photo_layout:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        HeadImageUtils.getFromCamara(getActivity());
                        if (picWindow != null && picWindow.isShowing()) {
                            picWindow.dismiss();
                        }
                    } else {
                        Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            Log.d("plat", "platform" + platform);
            Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

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

    private String downUrl = "";
    private String newVersionName = "";
    private String versionRemark = "";

    private class VersionUpdateServiceTask extends AsyncTask<Void, Void, VersionUpdateServiceRet> {
        boolean isAutoUpdate = true;

        public VersionUpdateServiceTask(boolean isAutoUpdate) {
            this.isAutoUpdate = isAutoUpdate;
        }

        private onAlertDialogBtnClickListener clickListener = new onAlertDialogBtnClickListener() {

            @Override
            public void onOk() {
                customWidgets.hideAlertDialog();
                // startActivity(new Intent(context,
                // UpdateActivity.class));

                if (downUrl != null && downUrl.length() > 0) {
                    Intent intent = new Intent(context, UpdateService.class);
                    intent.putExtra(Contants.COMMON_URL, downUrl);
                    context.startService(intent);
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
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // if (!isAutoUpdate) {
            // customWidgets.showProgressDialog("正在获取版本信息...");
            // }
        }

        @Override
        protected void onPostExecute(VersionUpdateServiceRet result) {
            super.onPostExecute(result);
            // customWidgets.hideProgressDialog();
            if (result != null) {
                if (result.errCode.equals(StatusCode.STATUS_CODE_SUCCESS)) {
                    if (result.data.version == null) {
                        if (!isAutoUpdate) {
                            AlertUtil.show(context, "没有版本信息!");
                        }
                        return;
                    }

                    if (result.data.version.equals(CommUtils.getVersionName(context))) {
                        if (!isAutoUpdate) {
                            customWidgets.showInfoDialog("检查更新",
                                    "当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(context));
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
                    // AlertUtil.show(context, result.body.remark);
                }
            } else {
                if (isValidContext(context)) {
                    AlertUtil.show(context, context.getResources().getString(R.string.net_connect_error));
                }
            }
        }

        private void showUpdateDialog() {
            customWidgets.showAlertDialog("检查更新", "腾牛君又发新版本啦!\n赶快来尝鲜 ? ", clickListener);
        }

        @Override
        protected VersionUpdateServiceRet doInBackground(Void... params) {
            try {
                return ServiceInterface.VersionUpdateService(context, getMetaDataValue("UMENG_CHANNEL"));
            } catch (Exception e) {
            }
            return null;
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

    private String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
    }

    public void setUserState(){
        User user = (User) PreferencesUtils.getObject(context, "login_user", User.class);
        App.loginUser = user;
        if (App.loginUser != null) {
            KJBitmap kjb = new KJBitmap();
            kjb.display(userImage, user.logo);//设置用户头像
            userNameTv.setText(StringUtils.isEmpty(user.nickname)?"火星用户":user.nickname);
            if(user.is_vip == 1){
                vipImageView.setVisibility(View.VISIBLE);
            }
            lineView.setVisibility(View.VISIBLE);
            loginOutTv.setVisibility(View.VISIBLE);
        }else{
            vipImageView.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            loginOutTv.setVisibility(View.GONE);
            userNameTv.setText(getString(R.string.default_user_name_text));
            userImage.setImageResource(R.mipmap.user_default_img);
            userImage.setBorderThickness(1);
        }
    }


    /**
     * 获取用户信息
     */
    public void getUserInfo() {

        setUserState();

        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        params.put("mime", App.ANDROID_ID);
        kjh.post(Server.URL_GET_USER_MESSAGE, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] bt) {
                super.onSuccess(headers, bt);
                // 获取cookie
                KJLoger.debug("===" + headers.get("Set-Cookie"));

                if (bt != null && bt.length > 0) {
                    String result = new String(bt);
                    if (result != null) {
                        try {
                            UserRet userRet = Contants.gson.fromJson(result, new TypeToken<UserRet>() {
                            }.getType());
                            if (Result.checkResult(getActivity(), userRet)) {
                                /*if(userRet.data != null && userRet.data.name != null){
									tempUserName = userRet.data.name;
									if(userRet.data.avatar != null && userRet.data.avatar.length() > 0 && userImage != null){
										KJBitmap kjb = new KJBitmap();
										kjb.display(userImage,userRet.data.avatar);//设置用户头像

										if(getActivity() != null && ((MainActivity)getActivity()).mHomeFragment != null){
											((MainActivity)getActivity()).mHomeFragment.setUserImage(userRet.data.avatar);
										}
									}
								}*/
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } finally {
                            userNameTv.setText(tempUserName);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                userNameTv.setText(tempUserName);
            }
        });
    }

    /**
     * 修改用户图像
     */
    public void updateImg() {
        if (picWindow != null && picWindow.isShowing()) {
            picWindow.dismiss();
        }
        if (userImage != null && HeadImageUtils.imgPath != null) {
            KJBitmap kjb = new KJBitmap();
            kjb.display(userImage, HeadImageUtils.imgPath);//设置用户头像
        }
    }

    /**
     * 修改用户名成功之后,刷新用户名称
     */
    @Override
    public void refreshUserNameUI(String userName) {
        userNameTv.setText(userName);
    }

}
