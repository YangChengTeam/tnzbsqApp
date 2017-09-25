package com.fy.tnzbsq.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.tnzbsq.activity.CommunityFragment;
import com.fy.tnzbsq.activity.CustomBaseFragment;
import com.fy.tnzbsq.activity.CustomWebFragment;
import com.fy.tnzbsq.activity.CustomWebOtherFragment;
import com.fy.tnzbsq.activity.MyInfoFragment;
import com.fy.tnzbsq.activity.ZBFragment;
import com.orhanobut.logger.Logger;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private int size;

    private ZBFragment zbFragment;

    private CustomWebOtherFragment customWebOtherFragment;
    private CommunityFragment communityFragment;

    private CustomBaseFragment baseFragment;

    private CustomWebFragment customWebFragment;

    public MyViewPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Logger.e("pos---> 0");
                baseFragment = new ZBFragment();
                break;
            case 1:
                Logger.e("pos---> 1");
                baseFragment = new CustomWebOtherFragment();
                break;
            case 2:
                Logger.e("pos---> 2");
                baseFragment = new CustomBaseFragment();
                break;
            case 3:
                Logger.e("pos---> 3");
                baseFragment = new CommunityFragment();
                break;
            case 4:
                Logger.e("pos---> 4");
                baseFragment = new MyInfoFragment();
                break;
            default:
                Logger.e("pos---> def");
                baseFragment = new CustomWebFragment();
                break;
        }

        return baseFragment;
    }

    @Override
    public int getCount() {
        return size;
    }
}
