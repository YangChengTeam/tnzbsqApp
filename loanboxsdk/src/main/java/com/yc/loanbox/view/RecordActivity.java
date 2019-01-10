package com.yc.loanbox.view;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.view.adpater.ProductAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class RecordActivity extends BaseActivity {



    @BindView(R2.id.recyclerView)
    RecyclerView mProductRecyclerView;

    private ProductAdapter productAdapter;


    @BindView(R2.id.type_name)
    TextView tv_type_name;

    @BindView(R2.id.back_btn)
    ImageView backImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_record;
    }

    @Override
    protected void initViews() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(R.layout.loanbox_item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                if(productInfo != null){
                    productInfo.setPtype(Config.TYPE104);
                }
                startWebActivity(productInfo);
            }
        });


        RxView.clicks(backImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            finish();
        });
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData(){
        this.showLoadingDialog( "加载中...");
        List<ProductInfo> productInfos = null;
        String json = MMKV.defaultMMKV().getString("record", "");
        if(!TextUtils.isEmpty(json)){
            productInfos = JSON.parseObject(json, new TypeReference<List<ProductInfo>>() {
            }.getType());
        }
        if(productInfos != null){
            productAdapter.setNewData(productInfos);
        }
        this.dissmissLoadingDialog();
    }
}
