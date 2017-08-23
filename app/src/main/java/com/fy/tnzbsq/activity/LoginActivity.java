package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.kymjs.kjframe.ui.BindView;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class LoginActivity extends BaseActivity {

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

            HashMap<String, String> params = new HashMap<>();
            params.put("imeil", App.ANDROID_ID);
            params.put("open_id", user.user_id);
            params.put("nickname", user.nickname);
            params.put("gender", user.gender);
            params.put("logo", user.logo);
            params.put("login_type", user.login_type);

            HttpCoreEngin.get(context).rxpost(Server.URL_QX_LOGIN, new TypeReference<ResultInfo<User>>() {
            }.getType(), params, true, true, false).subscribe(new Subscriber<ResultInfo<User>>() {
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

                    if (userRet.code == 0) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        PreferencesUtils.putObject(LoginActivity.this, "login_user", userRet.data);
                        App.loginUser = userRet.data;
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "登录失败，请稍后重试", Toast.LENGTH_LONG).show();
                    }
                }
            });

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
                dialog.show();
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
                break;
            case R.id.weixin_auth_login_btn:
                dialog.show();
                platform = SHARE_MEDIA.WEIXIN;
                mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
                break;
            default:
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
