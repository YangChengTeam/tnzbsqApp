package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.GifDataAdapter;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.bean.GifDataInfo;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.jaeger.library.StatusBarUtil;
import com.kk.pay.other.ToastUtil;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 首页-分类、Banner页
 */
public class GifMakeListActivity extends BaseAppActivity {

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.category_list)
    RecyclerView mCategoryRecyclerView;

    GifDataAdapter mGifDataAdapter;

    List<GifDataInfo> list;

    private int[] gifs = {R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4, R.mipmap.g5, R.mipmap.g6};

    private String[] gifValues = {"为所欲为", "王境泽", "土拨鼠", "偷电动车", "不可能打工", "金坷垃"};

    private String[] urls = {
            "https://soy.qqtn.com/sorry/",
            "https://soy.qqtn.com/wangjingze/",
            "https://soy.qqtn.com/marmot/",
            "https://soy.qqtn.com/diandongche/",
            "https://soy.qqtn.com/dagong/",
            "https://soy.qqtn.com/jinkela/",
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gif_list;
    }

    @Override
    protected void initVars() {

        StatusBarUtil.setColor(GifMakeListActivity.this, getResources().getColor(R.color.colorAccent), 0);
        mToolbar.setTitle("热门Gif图制作");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        for (int i = 0; i < gifs.length; i++) {
            list.add(new GifDataInfo(gifValues[i], gifs[i]));
        }

        mGifDataAdapter = new GifDataAdapter(GifMakeListActivity.this, list);
        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mCategoryRecyclerView.setAdapter(mGifDataAdapter);

        mGifDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(GifMakeListActivity.this, GifMakeActivity.class);
                intent.putExtra("title", gifValues[position]);
                intent.putExtra("url", urls[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

}
