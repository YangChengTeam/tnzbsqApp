package com.fy.tnzbsq.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.LoginActivity;
import com.fy.tnzbsq.activity.MainActivity;
import com.fy.tnzbsq.activity.SearchActivity;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.view.TabLineLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.widget.RoundImageView;

public abstract class BaseFragment extends Fragment implements OnClickListener, TabLineLayout.TabDelegate {
    protected Context ct;
    /**
     * SlidingMenu对象
     */
    protected SlidingMenu sm;
    public View rootView;
    protected Activity MenuChangeHome;
    /**
     * 左菜单按钮
     */
    private ImageButton leftMenuBtn;
    /**
     * 左菜单按钮
     */
    private RoundImageView leftMenuUserImg;
    /**
     * 右菜单按钮
     */
    private ImageView rightMenuBtn;

    private int currentIndex;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        sm = ((MainActivity) getActivity()).getSlidingMenu();
        initData(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ct = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // RxBus.get().register(this);
        rootView = initView(inflater);
        leftMenuBtn = (ImageButton) rootView.findViewById(R.id.main_title_more_icon);
        leftMenuUserImg = (RoundImageView) rootView.findViewById(R.id.main_user_icon);
        rightMenuBtn = (ImageView) rootView.findViewById(R.id.main_search_icon);
        leftMenuBtn.setOnClickListener(this);
        leftMenuUserImg.setOnClickListener(this);
        rightMenuBtn.setOnClickListener(this);
        setListener();
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        User user = (User) PreferencesUtils.getObject(ct, "login_user", User.class);
        App.loginUser = user;
        if (App.loginUser != null) {
            setUserImage(App.loginUser.logo);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.main_title_more_icon: // 点击左边的按钮，左菜单收放
            case R.id.main_user_icon: // 点击左边的按钮，左菜单收放
                if (App.loginUser != null) {
                    sm.toggle();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.main_search_icon: // 点击右边按钮，右菜单缩放
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                /*if (currentIndex == 0) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
                if (currentIndex == 2) {
                    RxBus.get().post(Contants.COMMUNITY_ADD, "1");
                }*/
                break;
            default:
                break;
        }
    }

    /**
     * 初始化UI
     *
     * @param inflater
     * @return
     */
    protected abstract View initView(LayoutInflater inflater);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();


    public void tabFragment(int index) {
        currentIndex = index;
        if (index == 0) {
            if (rightMenuBtn.getVisibility() == View.GONE) {
                rightMenuBtn.setVisibility(View.VISIBLE);
            }
        }
        if (index == 2) {
            if (rightMenuBtn.getVisibility() == View.VISIBLE) {
                rightMenuBtn.setVisibility(View.GONE);
            }
        }
    }

    public void setUserImage(String imagePath) {
        KJBitmap kjb = new KJBitmap();
        kjb.display(leftMenuUserImg, imagePath, DensityUtils.dip2px(ct, 30), DensityUtils.dip2px(ct, 30));
        leftMenuUserImg.setBorderThickness(1);
    }

    /**
     * 修改用户图像
     */
    public void updateImg() {

        KJBitmap kjb = new KJBitmap();
        kjb.display(leftMenuUserImg, HeadImageUtils.imgPath);//设置用户头像
    }

}
