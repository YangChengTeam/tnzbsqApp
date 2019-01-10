package com.yc.loanbox.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ExchangeGalleryListEvent;
import com.yc.loanbox.model.bean.IndexInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.RefreshTab1Event;
import com.yc.loanbox.model.bean.RefreshTab2Event;
import com.yc.loanbox.view.fragment.GalleryFragment;
import com.yc.loanbox.view.fragment.IndexFragment;
import com.yc.loanbox.view.fragment.MyFragment;
import com.yc.loanbox.view.fragment.ProductGridFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindViews;


public class LoanboxMainActivity extends BaseActivity implements View.OnClickListener {

    private Fragment[] fragments;

    @BindViews({R2.id.tab1_icon, R2.id.tab2_icon, R2.id.tab3_icon, R2.id.tab4_icon})
    public List<ImageView> tab_imageViews;

    @BindViews({R2.id.tab1_text, R2.id.tab2_text, R2.id.tab3_text, R2.id.tab4_text})
    public List<TextView> tab_textViews;


    private boolean isClick1 = true;
    private boolean isClick2 = false;
    private boolean isClick3 = false;
    private boolean isClick4 = false;

    public IndexInfo indexInfo;

    public boolean isFor;
    public boolean isFormJPush;
    public ProductInfo productInfo;

    private static LoanboxMainActivity loanboxMainActivity;

    public static LoanboxMainActivity getLoanboxMainActivity() {
        return loanboxMainActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFor = true;
        if (isFormJPush && productInfo != null) {
            productInfo.setPtype(Config.TYPE105);
            startWebActivity(productInfo);
            productInfo = null;
            isFormJPush = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFor = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_loanboxmain;
    }

    @Override
    protected void initViews() {

        loanboxMainActivity = this;

        Fragment fragment1 = IndexFragment.newInstance();
        Fragment fragment2 = ProductGridFragment.newInstance();
        Fragment fragment3 = GalleryFragment.newInstance();
        Fragment fragment4 = MyFragment.newInstance();
        this.fragments = new Fragment[]{fragment1, fragment2, fragment3, fragment4};
        if (!fragment1.isAdded()) {
            getSupportFragmentManager().beginTransaction().add((int) R.id.fragment_layout, fragment1).show(fragment1).commit();
        }
        view_edit(0);

        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.tab3).setOnClickListener(this);
        findViewById(R.id.tab4).setOnClickListener(this);
    }


    public void onClick(View v) {
        onTabSelect(v);
    }

    public void onTabSelect(View view) {
        int id = view.getId();
        if (id == R.id.tab1) {
            if (this.fragments != null && this.fragments.length > 0) {
                showFragment(1);
                return;
            }
        } else if (id == R.id.tab2) {
            if (this.fragments != null && this.fragments.length > 0) {
                showFragment(2);
                return;
            }
        } else if (id == R.id.tab3) {
            if (this.fragments != null && this.fragments.length > 0) {
                showFragment(3);
                return;
            }
        } else if (id == R.id.tab4) {
            if (this.fragments != null && this.fragments.length > 0) {
                showFragment(4);
                return;
            }
            return;
        }

    }

    public void showFragment(int index) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        if (index == 1) {
            trx.hide(this.fragments[1]).hide(this.fragments[2]).hide(this.fragments[3]);
            if (!(this.fragments[0].isAdded() || this.isClick1)) {
                this.isClick1 = true;
                this.isClick2 = false;
                this.isClick3 = false;
                this.isClick4 = false;
                trx.add((int) R.id.fragment_layout, this.fragments[0]);
            }
            trx.show(this.fragments[0]).commit();
            if (this.fragments[0].isVisible()) {
                EventBus.getDefault().post(new RefreshTab1Event());
            }
        } else if (index == 2) {
            trx.hide(this.fragments[0]).hide(this.fragments[2]).hide(this.fragments[3]);
            if (!(this.fragments[1].isAdded() || this.isClick2)) {
                this.isClick1 = false;
                this.isClick2 = true;
                this.isClick3 = false;
                this.isClick4 = false;
                trx.add((int) R.id.fragment_layout, this.fragments[1]);
            }
            trx.show(this.fragments[1]).commit();
            if (this.fragments[1].isVisible()) {
                EventBus.getDefault().post(new RefreshTab2Event());
            }
        } else if (index == 3) {
            trx.hide(this.fragments[0]).hide(this.fragments[1]).hide(this.fragments[3]);
            if (!(this.fragments[2].isAdded() || this.isClick3)) {
                this.isClick1 = false;
                this.isClick2 = false;
                this.isClick3 = true;
                this.isClick4 = false;
                trx.add((int) R.id.fragment_layout, this.fragments[2]);
            }
            trx.show(this.fragments[2]).commit();
            EventBus.getDefault().post(new ExchangeGalleryListEvent());

        } else if (index == 4) {
            trx.hide(this.fragments[0]).hide(this.fragments[1]).hide(this.fragments[2]);
            if (!(this.fragments[3].isAdded() || this.isClick4)) {
                this.isClick1 = false;
                this.isClick2 = false;
                this.isClick3 = false;
                this.isClick4 = true;
                trx.add((int) R.id.fragment_layout, this.fragments[3]);
            }
            trx.show(this.fragments[3]).commit();
        }
        view_edit(index - 1);
    }

    @Override
    protected void initData() {
        super.initData();
        if (LoanApplication.getLoanApplication().init_img != null) {
            startWebActivity(LoanApplication.getLoanApplication().init_img);
        }
    }

    private void view_edit(int index) {
        for (int i = 0; i < this.tab_textViews.size(); i++) {
            TextView textView = (TextView) this.tab_textViews.get(i);
            int res;
            if (i == index) {
                textView.setTextColor(getResources().getColor(R.color.tab_color));
                res = R.mipmap.tab_shuaxin;
                if (index == 0) {
                    textView.setText("刷新");
                } else if (index == 1) {
                    res = R.mipmap.tab_shuaxin;
                    textView.setText("刷新");
                } else if (index == 2) {
                    res = R.mipmap.ic_tab4;
                    textView.setText(getResources().getString(R.string.main_tab3_text));
                } else if (index == 3) {
                    res = R.mipmap.ic_tab5;
                    textView.setText(getResources().getString(R.string.main_tab5_text));
                }
                ((ImageView) this.tab_imageViews.get(i)).setImageResource(res);
            } else {
                textView.setTextColor(getResources().getColor(R.color.tab_default_color));
                res = R.mipmap.ic_tab1_default;
                if (i == 0) {
                    textView.setText(getResources().getString(R.string.main_tab1_text));
                } else if (i == 1) {
                    res = R.mipmap.ic_tab2_default;
                    textView.setText(getResources().getString(R.string.main_tab2_text));
                } else if (i == 2) {
                    res = R.mipmap.ic_tab3_default;
                    textView.setText(getResources().getString(R.string.main_tab3_text));
                } else if (i == 3) {
                    res = R.mipmap.ic_tab5_default;
                    textView.setText(getResources().getString(R.string.main_tab5_text));
                }
                ((ImageView) this.tab_imageViews.get(i)).setImageResource(res);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loanboxMainActivity = null;
    }
}
