package com.yc.loanbox.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.view.adpater.ArticleAdapter;

import butterknife.BindView;

public class ArticleInfoFragment extends BaseFragment {

    @BindView(R2.id.recyclerView)
    RecyclerView mArticleRecyclerView;

    private ArticleAdapter articleAdapter;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static Fragment newInstance() {
        return new ArticleInfoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_article;
    }

    @Override
    protected void initViews() {

    }
}
