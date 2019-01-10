package com.yc.loanbox.view.adpater;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.loanbox.R;
import com.yc.loanbox.model.bean.ArticleInfo;

import java.util.List;

public class ArticleAdapter extends BaseQuickAdapter<ArticleInfo, BaseViewHolder> {

    public ArticleAdapter(@Nullable List data) {
        super(R.layout.loanbox_item_article, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInfo item) {

    }
}
