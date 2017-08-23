package com.fy.tnzbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.TabLineLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import org.kymjs.kjframe.utils.SystemTool;
import org.kymjs.kjframe.widget.KJSlidingMenu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Main4Activity extends FragmentActivity implements TabLineLayout.TabDelegate{

	public static String uninstallCallUrl = "http://fy.u9u9.com/spread/uninstall.php";
	
	public static String TAG = "Main4Activity";
	
	private List<CustomBaseFragment> customWebFragments;

	private FragmentManager fragmentManager;

	private TabLineLayout tabLineLayout;
	public int currentIndex;

	private long clickTime = 0;

	private ImageView searchImg;
	
//	private ImageView shareImg;
//
//	private TextView shareTv;
//
//	private LinearLayout shareLayout;
	
	public boolean isShow = false;
	
	private CustomBaseFragment currentFragment = null;
	
	private CurrentTabIndex currentTabIndex;
	
	private KJSlidingMenu mSliding;
	
	//<<< C端fork进程
    static {
        System.loadLibrary("fycore");
    }
	
	public interface CurrentTabIndex {
		void currentSelect(int index);
	}
	
	public void setCurrentTabIndex(CurrentTabIndex currentTabIndex) {
		this.currentTabIndex = currentTabIndex;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		mSliding = (KJSlidingMenu)findViewById(R.id.main_group);
		searchImg = (ImageView)findViewById(R.id.search_img);
//		shareImg = (ImageView)findViewById(R.id.share_img);
//		shareTv = (TextView)findViewById(R.id.share_text);
//		shareLayout = (LinearLayout)findViewById(R.id.share_layout);
		
		currentIndex = -1;

		fragmentManager = getSupportFragmentManager();
		initCustomBaseFragments();

		tabLineLayout = (TabLineLayout) findViewById(R.id.tab);
		tabLineLayout.delegate = this;
		tabLineLayout.check(0);
		
		CustomProgress dialog = CustomProgress.create(Main4Activity.this, "正在分享...", true, null);
        dialog.setTitle("装B神器分享");
		
//        shareLayout.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				/*UMImage image = new UMImage(Main4Activity.this, R.drawable.logo_108);
//				final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//						SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE };
//				new ShareAction(Main4Activity.this).setDisplayList(displaylist).withText("装B神器")
//						.withTargetUrl("http://zs.qqtn.com").withMedia(image).setListenerList(umShareListener)
//						.open();*/
//				

//			}
//		});
		
        searchImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main4Activity.this, SearchActivity.class);
				startActivity(intent);
			}
		});
        
        uninstallOb();
	}

	private UMShareListener umShareListener = new UMShareListener() {

		/**
		 * @descrption 分享开始的回调
		 * @param platform 平台类型
		 */
		@Override
		public void onStart(SHARE_MEDIA platform) {
			SocializeUtils.safeShowDialog(null);
		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat", "platform" + platform);
			Toast.makeText(Main4Activity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(Main4Activity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(Main4Activity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};
	
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		UMShareAPI.get( this ).onActivityResult(arg0, arg1, arg2);
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		//customWebFragments.get(currentIndex).customWebView.delegate = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initCustomBaseFragments() {
		customWebFragments = new ArrayList<CustomBaseFragment>();
		String[] urls = new String[] { "file:///android_asset/index.html", "file:///android_asset/new_list.html",
				"file:///android_asset/tg.html", "file:///android_asset/my.html" };
		/*for (String url : urls) {
			CustomBaseFragment customWebFragment = new CustomBaseFragment();
			customWebFragment.url = url;
			customWebFragments.add(customWebFragment);
		}*/
		
		for(int i=0;i<4;i++){
			if(i == 0 || i == 1){
				CustomWebFragment customWebFragment = new CustomWebFragment();
				customWebFragment.url = urls[i];
				customWebFragments.add(customWebFragment);
			}
			if(i == 2 || i == 3){
				CustomWebOtherFragment customWebOtherFragment = new CustomWebOtherFragment();
				customWebOtherFragment.url = urls[i];
				customWebFragments.add(customWebOtherFragment);
			}
		}
		
	}

	public void tabFragment(int index) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		CustomBaseFragment fragment = customWebFragments.get(index);
		if (index == currentIndex) {
			if(!fragment.isAdded()){
				transaction.add(R.id.content, fragment).commitAllowingStateLoss();
			}else {
				if(fragment.customWebView != null){
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
		
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	private void exit() {
		isShow = customWebFragments.get(currentIndex).isShow;
		if(isShow){
			customWebFragments.get(currentIndex).customWebView.loadUrl("javascript:toClose();");
			isShow = false;
		}else{
			if ((System.currentTimeMillis() - clickTime) > 2000) {
				clickTime = System.currentTimeMillis();
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			} else {
				System.exit(0);
			}
		}
	}

	/*@Override
	public void Horizontal() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).Horizontal();
	}

	@Override
	public void networkSet() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).networkSet();
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).reload();
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).setTitle(title);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).hide();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).show();
	}

	@Override
	public void startFullActivity(GameInfo gameInfo) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).startFullActivity(gameInfo);
	}

	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).setUrl(url);
	}

	@Override
	public void showWheelView() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).showWheelView();
	}

	@Override
	public void createImage(String id, String data,String isVip,String price) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).createImage(id, data);
	}

	@Override
	public void saveImage(String imageRealPath) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).saveImage(imageRealPath);
	}

	@Override
	public void addKeep(String id) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).addKeep(id);
	}

	@Override
	public void imageShow(String path) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).imageShow(path);
	}

	@Override
	public void updateVersion() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).updateVersion();
	}

	@Override
	public void selectPic() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).selectPic();
	}

	@Override
	public void submitMesage(String str, String description) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).submitMesage(str, description);
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).clearCache();
	}

	@Override
	public void toSave() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).toSave();
	}

	@Override
	public void toShare() {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).toShare();
	}

	@Override
	public void initWithUrl(String url) {
		// TODO Auto-generated method stub
		customWebFragments.get(currentIndex).initWithUrl(url);
	}

	@Override
	public void updateUserName(String userName) {
		customWebFragments.get(currentIndex).updateUserName(userName);
	}

	@Override
	public void setShowState(boolean flag) {
		this.isShow = flag;
	}

	@Override
	public void loadImageList(int currentPage) {
		KJLoger.debug("main---loadImageList---");
		
		customWebFragments.get(currentIndex).loadImageList(currentPage);
		
	}*/
	
	private native int init(String userSerial, String packageName, String urlStr);
	
	private String getUserSerial() {
        Object userManager = getSystemService(Context.USER_SERVICE);
        if (userManager == null) {
            //Log.e(TAG, "userManager not exsit !!!");
            return null;
        }
        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            Log.v(TAG, userSerial+"");
            return String.valueOf(userSerial);
        }
        catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
        catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }
        catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
	
	private void uninstallOb(){
        String packageName = this.getPackageName();

        uninstallCallUrl += "?plaform=android&package="+packageName+"&device="+SystemTool.getPhoneIMEI(getApplicationContext());

        if (Build.VERSION.SDK_INT < 17) {
            init(null, packageName, uninstallCallUrl);
        }
        else {
            init(getUserSerial(), packageName, uninstallCallUrl);
        }
    }
	
}
