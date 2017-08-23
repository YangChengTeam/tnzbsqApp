package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.PriceRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.fragment.HomeFragment;
import com.fy.tnzbsq.fragment.LeftMenuFragment;
import com.fy.tnzbsq.service.MyService;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.CreatePopupWindow;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.TabLineLayout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.widget.KJSlidingMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends SlidingFragmentActivity implements TabLineLayout.TabDelegate {

    public final static String TAG = "MainActivity";

    public Context context;

    // 侧滑菜单
    private SlidingMenu sm;
    // 左边菜单
    private LeftMenuFragment mLeftMenu;
    // 主界面
    public HomeFragment mHomeFragment;
    // 保存Fragment的状态
    private Fragment mContent;

    private List<CustomBaseFragment> customWebFragments;

    private FragmentManager fragmentManager;

    private TabLineLayout tabLineLayout;

    public int currentIndex;

    private long clickTime = 0;

    public boolean isShow = false;

    private CustomBaseFragment currentFragment = null;

    private CurrentTabIndex currentTabIndex;

    private KJSlidingMenu mSliding;

    // 图片选择弹出窗口
    private CreatePopupWindow createWindow;

    public interface CurrentTabIndex {
        void currentSelect(int index);
    }

    public void setCurrentTabIndex(CurrentTabIndex currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "投稿成功!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "投稿失败,请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "缓存已清除", Toast.LENGTH_SHORT).show();
                    // 最后通知更新
                    context.sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Contants.BASE_SD_DIR)));
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    Toast.makeText(context, "下载地址有误，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sm = getSlidingMenu();
        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.menu_left_frag);

        if (savedInstanceState == null) {
            mLeftMenu = new LeftMenuFragment();
            mHomeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.menu_left_frag, mLeftMenu, "Left").commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mHomeFragment, "Home").commit();
        }

        sm.setShadowDrawable(R.drawable.shadow); // 设置阴影图片
        sm.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 显示主界面的宽度
        sm.setFadeDegree(0.35f); // SlidingMenu滑动时的渐变程度
        sm.setTouchModeAbove(SlidingMenu.LEFT); // 设置滑动的屏幕范围，该设置为全屏区域都可以滑动
        // sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setMode(SlidingMenu.LEFT);
        // sm.setMode(SlidingMenu.LEFT_RIGHT); // 设置菜单同时兼具左右滑动

        //currentIndex = -1;
        fragmentManager = getSupportFragmentManager();

        initCustomBaseFragments();

        tabLineLayout = (TabLineLayout) findViewById(R.id.tab);
        tabLineLayout.delegate = this;

        CustomProgress dialog = CustomProgress.create(context, "正在分享...", true, null);
        dialog.setTitle("装B神器分享");

        getPriceConfig();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new ActsAllDataTask().execute();
            }
        }, 5000);

        //启动一个服务
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);

        //wifi下，下载游戏,可免安装试玩
        /*if(NetUtil.isWIFIConnected(context)){
            new DownGameAsyncTask().execute();
        }*/
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
                            PriceRet result = Contants.gson.fromJson(resultValue, new TypeToken<PriceRet>() {}.getType());
                            if (result != null && result.errCode.equals("0")) {
                                App.siglePrice = result.data != null ? Float.parseFloat(result.data.single) : 2f;
                                App.vipPrice = result.data != null ? Float.parseFloat(result.data.vip) : 18f;

                                if(result.data != null && !StringUtils.isEmpty(result.data.singledesp)){
                                    App.sigleRemark = result.data.singledesp;
                                }else{
                                    App.sigleRemark = "付费解锁&(购买单个素材2元/个)";
                                }

                                if(result.data != null && !StringUtils.isEmpty(result.data.vipdesp)){
                                    App.vipRemark = result.data.vipdesp;
                                }else{
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case HeadImageUtils.FROM_CRAMA:
                if (HeadImageUtils.photoCamare != null) {
                    HeadImageUtils.cutCorePhoto(MainActivity.this, HeadImageUtils.photoCamare);
                }
                break;
            case HeadImageUtils.FROM_LOCAL:
                if (data != null && data.getData() != null) {
                    HeadImageUtils.cutCorePhoto(MainActivity.this, data.getData());
                }
                break;
            case HeadImageUtils.FROM_CUT:

                // 选择图像之后，修改用户的图像
                if (HeadImageUtils.cutPhoto != null) {
                /*
                 * if (HeadImageUtils.imgPath != null &&
				 * HeadImageUtils.imgPath.length() > 0) { updateUserInfo("", "",
				 * HeadImageUtils.imgPath); }
				 */
                    updateUserInfo("", "", HeadImageUtils.imgPath);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        // customWebFragments.get(currentIndex).customWebView.delegate = this;

        if (!App.start.isEmpty()) {
            if (App.start.equals("2")) {
                tabLineLayout.check(2);
            } else if (App.start.equals("0")) {
                tabLineLayout.check(0);
            } else {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("tags", App.start);
                context.startActivity(intent);
            }
            App.start = "";
        } else {
            tabLineLayout.check(currentIndex);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initCustomBaseFragments() {
        customWebFragments = new ArrayList<CustomBaseFragment>();
        String[] urls = new String[]{"file:///android_asset/index.html", "file:///android_asset/index.html",
                "file:///android_asset/index-dt.html", "file:///android_asset/index-dt.html"};
        /*
         * for (String url : urls) { CustomBaseFragment customWebFragment = new
		 * CustomBaseFragment(); customWebFragment.url = url;
		 * customWebFragments.add(customWebFragment); }
		 */

        for (int i = 0; i < 3; i++) {
            if (i == 0 || i == 1) {
                CustomWebFragment customWebFragment = new CustomWebFragment();
                customWebFragment.url = urls[i];
                customWebFragments.add(customWebFragment);
            }
            if (i == 2) {
                CustomWebOtherFragment customWebOtherFragment = new CustomWebOtherFragment();
                customWebOtherFragment.url = urls[i];
                customWebFragments.add(customWebOtherFragment);
            }
        }
    }

    public void tabFragment(int index) {
        if (index == 1) {
            // Toast.makeText(context, "选择了+号", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(context, CreateActivity.class);
            // startActivity(intent);

            createWindow = new CreatePopupWindow(context, itemsOnClick);
            createWindow.showAtLocation(findViewById(R.id.content_frame), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            CustomBaseFragment fragment = customWebFragments.get(index);
            if (index == currentIndex) {
                if (!fragment.isAdded()) {
                    transaction.add(R.id.content, fragment).commitAllowingStateLoss();
                } else {
                    if (fragment.customWebView != null) {
                        fragment.customWebView.scrollBy(0, 0);
                    }
                }
                return;
            }
            if (currentIndex >= 0) {
                currentFragment = customWebFragments.get(currentIndex);
            }
            if (!fragment.isAdded()) {
                if (currentFragment == null) {
                    transaction.add(R.id.content, fragment).commitAllowingStateLoss();
                } else {
                    transaction.hide(currentFragment).add(R.id.content, fragment).commitAllowingStateLoss();
                }
            } else {
                transaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
            }

            currentIndex = index;
            currentTabIndex.currentSelect(index);


            if (mHomeFragment != null && index >= 0) {
                mHomeFragment.tabFragment(index);
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        isShow = customWebFragments.get(currentIndex).isShow;
        if (isShow) {
            customWebFragments.get(currentIndex).customWebView.loadUrl("javascript:toClose();");
            isShow = false;
        } else {
            if ((System.currentTimeMillis() - clickTime) > 2000) {
                clickTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            } else {
                System.exit(0);
            }
        }
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_layout:
                    if (createWindow != null && createWindow.isShowing()) {
                        createWindow.closePopwindow();
                        //createWindow.dismiss();
                    }
                    break;
                case R.id.create_image_layout:
                    Intent intent = new Intent(context, ImageDiyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.create_word_layout:
                    Intent intentWord = new Intent(context, WordDiyActivity.class);
                    startActivity(intentWord);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 切换到主界面
     *
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    /**
     * 保存Fragment的状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // getSupportFragmentManager().putFragment(outState, "Home", mContent);
    }

    @SuppressLint("InlinedApi")
    private String getUserSerial() {
        Object userManager = getSystemService(Context.USER_SERVICE);
        if (userManager == null) {
            return null;
        }
        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser",
                    myUserHandle.getClass());
            long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            Log.v(TAG, userSerial + "");
            return String.valueOf(userSerial);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 修改用户信息
     *
     * @param userName
     * @param qqNumber
     * @param 'imgPatth'
     */
    public void updateUserInfo(String userName, String qqNumber, String imgPath) {
        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();

        params.put("mime", App.ANDROID_ID);

        if (userName != null && userName.length() > 0) {
            params.put("name", userName);
        }

        if (qqNumber != null && qqNumber.length() > 0) {
            params.put("qq", qqNumber);
        }

        if (imgPath != null && imgPath.length() > 0) {
            params.put("avatar", new File(imgPath));
        }

        params.putHeaders("Cookie", "cookie_tnzbsq");
        kjh.post(Server.URL_UPDATE_USER, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(t);
                if (t != null && t.length > 0) {
                    // String result = new String(t);
                    /*
					 * UserRet userRet = Contants.gson.fromJson(result, new
					 * TypeToken<UserRet>() { }.getType()); if
					 * (Result.checkResult(UserInfoActivity.this, userRet)) {
					 * Toast.makeText(UserInfoActivity.this, "修改成功",
					 * Toast.LENGTH_SHORT).show(); }
					 */

                    // Toast.makeText(context, "修改成功",
                    // Toast.LENGTH_SHORT).show();


                    if (mLeftMenu != null) {
                        mLeftMenu.updateImg();
                    }

                    if (mHomeFragment != null) {
                        mHomeFragment.updateImg();
                    }

                } else {
                    Toast.makeText(context, "修改用户信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取所有装逼类数据
     *
     * @author admin
     */
    private class ActsAllDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return ServiceInterface.getAllData(context);
        }

        @Override
        protected void onPostExecute(String result) {

            boolean newDataResult = true;

            try {
                super.onPostExecute(result);

                if (result != null) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.NEW_DATA_FILENAME);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();

                        try {
                            result = " var data= " + result;//重新封装
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(result.getBytes());
                            fos.close();

                            // 最后通知更新文件
                            if (file != null) {
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                            }
                        } catch (Exception e) {
                            newDataResult = false;
                        }
                    } else {
                        newDataResult = false;
                    }

                } else {
                    newDataResult = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                newDataResult = false;
            }
            PreferenceHelper.write(context, Contants.NEW_DATA_SHOW, Contants.NEW_DATA_RESULT, newDataResult);
        }
    }

    public static boolean downLoadGame() {
        boolean flag = false;

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {

            File fileDir = new File(Contants.GAME_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            final File file = new File(Contants.GAME_DOWN_FILE_PATH);
            URL url = new URL(Contants.GAME_INSTALL_URL);
            //LogUtil.msg("插件正在下载...");
            conn = (HttpURLConnection) url
                    .openConnection();
            if (file.exists() && file.length() == conn.getContentLength()) {
                flag = true;
                conn.disconnect();
            }
            is = conn.getInputStream();
            os = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int len;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
        return flag;
    }
}