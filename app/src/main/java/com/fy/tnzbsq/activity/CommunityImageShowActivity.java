package com.fy.tnzbsq.activity;

import android.os.Bundle;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.GalleryViewPager;
import com.fy.tnzbsq.adapter.UrlPagerAdapter;
import com.kk.pay.other.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/8/30.
 */

public class CommunityImageShowActivity extends BaseAppActivity {

    @BindView(R.id.view_pager)
    GalleryViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.community_show_image_list;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.getSerializable("images") != null) {
            List<String> items = (List<String>) bundle.getSerializable("images");
            if (items != null && items.size() > 0) {
                UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(CommunityImageShowActivity.this, items);
                viewPager.setOffscreenPageLimit(3);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(bundle.getInt("current_position", 0));
            }
        } else {
            ToastUtil.toast(this, "图片地址有误，请稍后重试");
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
