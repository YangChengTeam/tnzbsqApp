package com.yc.loanbox.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.view.fragment.ArticleInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ArticleListActivity extends BaseActivity {
    private ContentPagerAdapter contentAdapter;

    @BindView(R2.id.vp_content)
    ViewPager mContentVp;

    @BindView(R2.id.tl_tab)
    TabLayout mTab;

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_article_list;
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        private  BaseActivity mContent;
        private List<String> mFragmentTitles;
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
    protected void initViews() {
        this.mContentVp.setOffscreenPageLimit(3);
        this.contentAdapter = new ContentPagerAdapter(this, getSupportFragmentManager());
        this.contentAdapter.addFragment(ArticleInfoFragment.newInstance(), "全部");
        this.contentAdapter.addFragment(ArticleInfoFragment.newInstance(), "最新上线");
        this.contentAdapter.addFragment(ArticleInfoFragment.newInstance(), "今日上新");
        this.contentAdapter.addFragment(ArticleInfoFragment.newInstance(), "明日预告");
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
}
