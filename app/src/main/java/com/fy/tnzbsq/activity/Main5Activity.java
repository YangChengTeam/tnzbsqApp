package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.MyViewPagerAdapter;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.view.CreatePopupWindow;
import com.fy.tnzbsq.view.SpecialNoTitleTab;
import com.fy.tnzbsq.view.SpecialTab;
import com.fy.tnzbsq.view.SpecialTabRound;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

/**
 * Created by admin on 2017/9/11.
 */

public class Main5Activity extends BaseAppActivity implements SpecialNoTitleTab.MainAddListener {

    // 图片选择弹出窗口
    private CreatePopupWindow createWindow;

    private long clickTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVars() {
    }

    @Override
    protected void initViews() {
        PageNavigationView tab = (PageNavigationView) findViewById(R.id.tab);

        SpecialNoTitleTab noTitleItem = (SpecialNoTitleTab) NoTitleItem(R.mipmap.add_icon, R.mipmap.close_icon, "");
        noTitleItem.setListener(this);

        NavigationController navigationController = tab.custom()
                .addItem(newItem(R.mipmap.main_acts_normal, R.mipmap.main_acts_selected, "装逼"))
                .addItem(newItem(R.mipmap.main_fight_normal, R.mipmap.main_fight_selected, "斗图"))
                .addItem(noTitleItem)
                .addItem(newItem(R.mipmap.main_note_normal, R.mipmap.main_note_selected, "广场"))
                .addItem(newItem(R.mipmap.main_my_normal, R.mipmap.main_my_selected, "我的"))
                .build();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), navigationController.getItemCount()));

        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {

            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void addListener() {
        createWindow = new CreatePopupWindow(context, itemsOnClick);
        createWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, SizeUtils.getNavigationBarHeight(context));
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_layout:
                    if (createWindow != null && createWindow.isShowing()) {
                        createWindow.closePopwindow();
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
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF767676);
        mainTab.setTextCheckedColor(0xFFFE5000);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF767676);
        mainTab.setTextCheckedColor(0xFFFE5000);
        return mainTab;
    }

    /**
     * 无标题
     */
    private BaseTabItem NoTitleItem(int drawable, int checkedDrawable, String text) {
        SpecialNoTitleTab mainTab = new SpecialNoTitleTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        return mainTab;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {

        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            System.exit(0);
        }

    }
}
