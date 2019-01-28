package com.fy.tnzbsq.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ToastUtils;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.util.TimeUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.kymjs.kjframe.ui.BindView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.PermissionRequest;
import rx.Subscriber;


public class LoginActivity extends BaseActivity {

    private static int PER_CODE = 123;

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.qq_auth_login_btn, click = true)
    private ImageView qqLoginBtn;

    @BindView(id = R.id.weixin_auth_login_btn, click = true)
    private ImageView weixinLoginBtn;

    private UMShareAPI mShareAPI = null;

    SHARE_MEDIA platform = null;

    CustomProgress dialog;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        super.initData();
        mShareAPI = UMShareAPI.get(LoginActivity.this);
        titleNameTv.setText(getResources().getString(R.string.app_name));

        dialog = CustomProgress.create(LoginActivity.this, "正在登录...", true, null);
        dialog.setTitle("用户登录");
    }

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthUserInfoListener);
            //Toast.makeText(LoginActivity.this, "登录成功" + platform.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(LoginActivity.this, "登录授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthUserInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            User user = new User();
            if (platform.equals(SHARE_MEDIA.QQ)) {
                user.user_id = !StringUtils.isEmpty(data.get("openid")) ? data.get("openid").toString() : "";
                user.nickname = !StringUtils.isEmpty(data.get("screen_name")) ? data.get("screen_name").toString() : "";
                user.logo = !StringUtils.isEmpty(data.get("profile_image_url")) ? data.get("profile_image_url").toString() : "";
                user.gender = !StringUtils.isEmpty(data.get("gender")) ? data.get("gender").toString() : "";
                user.login_type = "1";
                Log.d("user info", "user info logo:" + data.get("profile_image_url").toString());//头像
            }

            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                user.user_id = !StringUtils.isEmpty(data.get("openid")) ? data.get("openid").toString() : "";
                user.nickname = !StringUtils.isEmpty(data.get("screen_name")) ? data.get("screen_name").toString() : "";
                user.logo = !StringUtils.isEmpty(data.get("profile_image_url")) ? data.get("profile_image_url").toString() : "";
                user.gender = !StringUtils.isEmpty(data.get("gender")) ? data.get("gender").toString() : "";
                user.login_type = "2";
            }
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("imeil", App.ANDROID_ID);
                params.put("open_id", user.user_id);
                params.put("nickname", URLEncoder.encode(user.nickname, "UTF-8"));
                params.put("gender", user.gender);
                params.put("logo", user.logo + "?time=" + TimeUtils.date2String(new Date()));
                params.put("login_type", user.login_type);

                HttpConfig.setPublickey(App.publicKey);
                HttpCoreEngin.get(context).rxpost(Server.URL_QX_LOGIN, new TypeReference<ResultInfo<User>>() {
                }.getType(), params, true, false, false).subscribe(new Subscriber<ResultInfo<User>>() {
                    @Override
                    public void onCompleted() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "登录失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ResultInfo<User> userRet) {
                        Log.e("login succes", "login success" + userRet.toString());

                        if (userRet.getCode() == 0) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            PreferencesUtils.putObject(LoginActivity.this, "login_user", userRet.getData());
                            App.loginUser = userRet.getData();
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败，请稍后重试", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(LoginActivity.this, "获取用户信息失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "取消获取用户信息", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.qq_auth_login_btn:
                requestPermission(1);
                break;
            case R.id.weixin_auth_login_btn:
                requestPermission(2);
                break;
            default:
                break;
        }
    }

    public void requestPermission(final int type) {
        XXPermissions.with(this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Manifest.permission.READ_PHONE_STATE) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            //ToastUtils.showLong("获取权限成功");
                            if (dialog != null && !dialog.isShowing()) {
                                dialog.show();
                                platform = type == 1 ? SHARE_MEDIA.QQ : SHARE_MEDIA.WEIXIN;
                                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
                            }
                        } else {
                            ToastUtils.showLong("部分权限未正常授予,请允许");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.showLong("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(LoginActivity.this);
                        } else {
                            ToastUtils.showLong("获取权限失败");
                        }
                    }
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
