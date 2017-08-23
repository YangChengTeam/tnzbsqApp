package com.fy.tnzbsq.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.kymjs.kjframe.ui.BindView;

public class AboutActivity extends BaseActivity implements CustomWebViewDelegate {

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.share_img, click = true)
    private ImageView shareImg;

    @BindView(id = R.id.share_text, click = true)
    private TextView shareTv;

    @BindView(id = R.id.webview)
    private CustomWebView customWebView;

    @BindView(id = R.id.join_tv, click = true)
    private TextView joinTv;

    @BindView(id = R.id.copy_tv, click = true)
    private TextView copyTv;

    private String url = "";

    @Override
    public void setRootView() {
        setContentView(R.layout.about);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        super.initData();

        titleNameTv.setText(getResources().getString(R.string.app_name));

        customWebView.delegate = this;
        customWebView.loadUrl("file:///android_asset/about.html");
        CustomProgress dialog = CustomProgress.create(AboutActivity.this, "正在分享...", true, null);
        dialog.setTitle("装B神器分享");
    }

    @Override
    public void widgetClick(View v) {
        UMImage image = new UMImage(AboutActivity.this, R.mipmap.logo_108);
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.share_img:

                break;
            case R.id.share_text:

                break;
            case R.id.join_tv:
                joinQQGroup("_KpIQPwGrQac_1gXm3WPb4l_vV8smP7A");
                break;
            case R.id.copy_tv:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, "1358808844"));
                Toast.makeText(this, "复制成功，可以加QQ了", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    /****************
     * 发起添加群流程。群号：腾牛装逼神器(462869974) 的 key 为： _KpIQPwGrQac_1gXm3WPb4l_vV8smP7A
     * 调用 joinQQGroup(_KpIQPwGrQac_1gXm3WPb4l_vV8smP7A) 即可发起手Q客户端申请加群 腾牛装逼神器(462869974)
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

    @Override
    public void Horizontal() {

    }

    @Override
    public void networkSet() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void startFullActivity(GameInfo gameInfo) {

    }

    @Override
    public void setUrl(String url) {
        if (!url.contains("no-wifi")) {
            this.url = url;
        }
    }

    @Override
    public void showWheelView() {

    }

    @Override
    public void createImage(String id, String data,String isVip,String price) {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveImage(String imageRealPath) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addKeep(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void imageShow(String path) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateVersion() {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectPic(int xvalue, int yvalue) {
        // TODO Auto-generated method stub

    }

    @Override
    public void submitMesage(String str, String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearCache() {
        // TODO Auto-generated method stub

    }

    @Override
    public void toSave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void toShare() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initWithUrl(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUserName(String userName) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setShowState(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadImageList(int currentPage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void search(String keyword) {
        // TODO Auto-generated method stub

    }

    @Override
    public void photoGraph() {
        // TODO Auto-generated method stub

    }

    @Override
    public void fightMenu() {
        // TODO Auto-generated method stub

    }


}
