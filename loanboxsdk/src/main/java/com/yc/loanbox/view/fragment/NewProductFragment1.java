package com.yc.loanbox.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.engin.TypeEngin;
import com.yc.loanbox.view.BaseActivity;
import com.yc.loanbox.view.NewMouthActivity;
import com.yc.loanbox.view.adpater.ProductAdapter;

import butterknife.BindView;

public class NewProductFragment1 extends BaseFragment {

    @BindView(R2.id.recycler_linear)
    RecyclerView mProductRecyclerView;

    private ProductAdapter productAdapter;

    TypeEngin typeEngin;


    public static Fragment newInstance() {
        return new NewProductFragment1();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_new_product;
    }

    @Override
    protected void initViews() {

        typeEngin = new TypeEngin(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(R.layout.loanbox_item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                if(productInfo != null){
                    productInfo.setPtype(Config.TYPE106);
                }
                ((BaseActivity)getActivity()).startWebActivity(productInfo);
            }
        });


    }

    @Override
    protected void initData() {
        NewMouthActivity newMouthActivity = (NewMouthActivity)getActivity();
        if(newMouthActivity.newListInfo!= null && newMouthActivity.newListInfo.getNew_list() != null) {
            View view = getLayoutInflater().inflate(R.layout.loanbox_head_view, (ViewGroup) mProductRecyclerView.getParent(), false);
            TextView textView = view.findViewById(R.id.time);
            textView.setText(newMouthActivity.newListInfo.getNew_list().getTime());
            productAdapter.addHeaderView(view);
            productAdapter.setNewData(newMouthActivity.newListInfo.getNew_list().getList());
            productAdapter.loadMoreEnd();
        }
    }

}
