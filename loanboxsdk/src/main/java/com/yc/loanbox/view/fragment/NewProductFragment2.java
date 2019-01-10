package com.yc.loanbox.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.model.bean.DayInfo;
import com.yc.loanbox.model.bean.ListInfo;
import com.yc.loanbox.view.NewMouthActivity;
import com.yc.loanbox.view.adpater.NewMouthAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewProductFragment2 extends BaseFragment {

    @BindView(R2.id.recycler_linear)
    RecyclerView mProductRecyclerView;

    NewMouthAdapter newMouthAdapter;

    private int tab;

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_new_product2;
    }

    public static Fragment newInstance(int tab) {
        NewProductFragment2 productFragment2 = new NewProductFragment2();
        productFragment2.tab = tab;
        return productFragment2;
    }

    @Override
    protected void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        newMouthAdapter = new NewMouthAdapter(null, tab);
        mProductRecyclerView.setAdapter(newMouthAdapter);
        newMouthAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    @Override
    protected void initData() {
        List<ListInfo> listInfos = new ArrayList<>();
        NewMouthActivity newMouthActivity = (NewMouthActivity)getActivity();
        DayInfo today = null;
        if(newMouthActivity.newListInfo != null) {
            if (tab == 0) {
                today = newMouthActivity.newListInfo.getToday();
            } else {
                today = newMouthActivity.newListInfo.getTomorrow();
            }
        }
        if(today != null) {
            listInfos.add(today.getTen_show());
            listInfos.add(today.getFourteen_show());
            listInfos.add(today.getSixteen_show());
            listInfos.add(today.getTwenty_show());
            newMouthAdapter.setNewData(listInfos);
            newMouthAdapter.loadMoreEnd();
        }
    }
}
