package com.yc.loanbox.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ScreenUtil;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.model.bean.NewListInfo;
import com.yc.loanbox.model.engin.NewMouthEngin;
import com.yc.loanbox.model.engin.NoticeEngin;
import com.yc.loanbox.view.fragment.NewProductFragment1;
import com.yc.loanbox.view.fragment.NewProductFragment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import rx.Subscriber;

public class NewMouthActivity extends BaseActivity {

    private NewMouthEngin newMouthEngin;
    private ContentPagerAdapter contentAdapter;


    @BindView(R2.id.ic_tx)
    ImageView txImageView;

    @BindView(R2.id.vp_content)
    ViewPager mContentVp;

    @BindView(R2.id.tab)
    TabLayout mTab;

    @BindViews({R2.id.wan, R2.id.qian, R2.id.bai, R2.id.shi, R2.id.ge})
    public List<TextView> num_texts;

    @BindView(R2.id.tx_btn)
    LinearLayout tx_btn;

    @BindView(R2.id.tx_text)
    TextView tx_text;

    @BindView(R2.id.total)
    TextView totalView;

    public NewListInfo newListInfo;


    class ContentPagerAdapter extends FragmentPagerAdapter {

        private  BaseActivity mContent;
        private  List<String> mFragmentTitles;
        private  List<Fragment> mFragments;

        public ContentPagerAdapter(BaseActivity content,FragmentManager fm) {
            super(fm);
            mFragmentTitles = new ArrayList<>();
            mFragments = new ArrayList<>();
            this.mContent = content;
        }


        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentTitles.add(title);
            mFragments.add(fragment);
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(mContent).inflate(R.layout.loanbox_tab_item, null);
            TextView tv = (TextView) v.findViewById(R.id.title);
            tv.setText(mFragmentTitles.get(position));
            return v;
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_new_mouth;
    }

    @Override
    protected void initViews() {
        boolean flag = MMKV.defaultMMKV().getBoolean("notice", false);
        setNotice(flag);
        RxView.clicks(findViewById(R.id.back_btn)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
             finish();
        });

        RxView.clicks(findViewById(R.id.tx_btn)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
             boolean _flag = MMKV.defaultMMKV().getBoolean("notice", false);
             MMKV.defaultMMKV().putBoolean("notice", !_flag);
             setNotice(!_flag);
             new NoticeEngin(NewMouthActivity.this).notice(!_flag ? 1 : 0).subscribe();
        });
    }

    private void setNotice(boolean flag){
        if(flag){
            tx_text.setTextColor(ContextCompat.getColor(NewMouthActivity.this, R.color.grey));
            txImageView.setImageDrawable(ContextCompat.getDrawable(NewMouthActivity.this, R.mipmap.ic_tx2));
            tx_btn.setBackground(ContextCompat.getDrawable(NewMouthActivity.this, R.mipmap.tx_bg2));

        } else {
            tx_text.setTextColor(ContextCompat.getColor(NewMouthActivity.this, R.color.white));
            txImageView.setImageDrawable(ContextCompat.getDrawable(NewMouthActivity.this, R.mipmap.ic_tx));
            tx_btn.setBackground(ContextCompat.getDrawable(NewMouthActivity.this, R.mipmap.tx_bg));
        }
    }

    private void setMainLayout(){
        this.mContentVp.setOffscreenPageLimit(3);
        this.contentAdapter = new ContentPagerAdapter(this, getSupportFragmentManager());
        this.contentAdapter.addFragment(NewProductFragment1.newInstance(), "最新上线");
        this.contentAdapter.addFragment(NewProductFragment2.newInstance(0), "今日上新");
        this.contentAdapter.addFragment(NewProductFragment2.newInstance(1), "明日预告");
        mContentVp.setAdapter(contentAdapter);

        this.mContentVp.setCurrentItem(0);
        this.mTab.getLayoutParams().height = ScreenUtil.getHeight(this) / 14;
        this.mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        this.mTab.setupWithViewPager(this.mContentVp);
        this.mTab.setTabMode(1);
        for (int i = 0; i < this.mTab.getTabCount(); i++) {
            this.mTab.getTabAt(i).setCustomView(this.contentAdapter.getTabView(i));
        }

    }

    @Override
    protected void initData() {
        showLoadingDialog("加载中...");
        newMouthEngin = new NewMouthEngin(this);
        newMouthEngin.getNewList().subscribe(new Subscriber<ResultInfo<NewListInfo>>() {
            @Override
            public void onCompleted() {
                dissmissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<NewListInfo> listResultInfo) {
                if(listResultInfo != null && listResultInfo.getCode() == 1){
                    newListInfo = listResultInfo.getData();
                    totalView.setText("今日共"+newListInfo.getTotal_num()+"个名额");

                    int surplus = Integer.valueOf(newListInfo.getSurplus_num());
                    int i = num_texts.size() - 1;
                    while(surplus>0)
                    {
                        int num = surplus % 10;
                        if(i > 0) {
                            num_texts.get(i--).setText(num + "");
                        }
                        surplus = surplus / 10;
                    }

                    setMainLayout();
                }
            }
        });
    }
}
