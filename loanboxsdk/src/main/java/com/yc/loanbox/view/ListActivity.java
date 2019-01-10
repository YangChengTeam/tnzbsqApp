package com.yc.loanbox.view;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.engin.TypeEngin;
import com.yc.loanbox.view.adpater.ProductAdapter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscriber;

public class ListActivity extends BaseActivity {

    @BindView(R2.id.recyclerView)
    RecyclerView mProductRecyclerView;

    private ProductAdapter productAdapter;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R2.id.message)
    TextView messageText;

    private TypeEngin typeEngin;
    private String type_id;

    private List<ProductInfo> productInfos;

    @BindView(R2.id.type_name)
    TextView tv_type_name;

    @BindView(R2.id.back_btn)
    ImageView backImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_list;
    }

    @Override
    protected void initViews() {
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(R.layout.loanbox_item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                productInfo.setPtype(type_id);
                setMessage(productInfo.getName());
                startWebActivity(productInfo);
            }
        });


        RxView.clicks(backImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            finish();
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if(intent != null){
            type_id = intent.getStringExtra("type_id");
            tv_type_name.setText(intent.getStringExtra("name"));
        }
        typeEngin = new TypeEngin(this);
        loadData();
    }

    private void loadData(){
        this.showLoadingDialog( "加载中...");
        typeEngin.getTypeInfo(type_id).subscribe(new Subscriber<ResultInfo<List<ProductInfo>>>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                dissmissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                dissmissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<List<ProductInfo>> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    productInfos = resultInfo.getData();
                    setMessage("");
                    productAdapter.setNewData(productInfos);
                    productAdapter.loadMoreEnd();
                }
            }
        });
    }

    class MyClickableSpan extends ClickableSpan {
        private ProductInfo productInfo;

        public MyClickableSpan(ProductInfo productInfo) {
            this.productInfo = productInfo;
        }

        public void onClick(View widget) {
            if(productInfo != null){
                productInfo.setPtype(Config.TYPE97);
            }
            startWebActivity(productInfo);
        }
    }

    private void setMessage(String click_app_name){
        if(productInfos == null) return;

        ProductInfo productInfo;
        String tmp_click_app_name = MMKV.defaultMMKV().getString("click_app_name", "");
        String next_click_app_name = "";
        int tmpIndx = 0;
        if(TextUtils.isEmpty(click_app_name)){
            if(!TextUtils.isEmpty(click_app_name)){
                click_app_name = tmp_click_app_name;
            } else {
                tmpIndx = new Random().nextInt(productInfos.size());
                click_app_name =  productInfos.get(tmpIndx).getName();
                MMKV.defaultMMKV().putString("click_app_name", click_app_name);
            }
        }
        int index = new Random().nextInt(productInfos.size());
        if(tmpIndx == index){
            if(index > 0) {
                index -= 1;
            }
            if(index == 0){
                index += 1;
            }
        }
        productInfo = productInfos.get(index);
        next_click_app_name = productInfo.getName();
        String str = "申请\"" + click_app_name + "\"的用户同时还申请了";
        SpannableString spannableString = new SpannableString(str + next_click_app_name);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), str.length(), spannableString.length(), 17);
        spannableString.setSpan(new UnderlineSpan(), str.length(), spannableString.length(), 17);
        spannableString.setSpan(new MyClickableSpan(productInfo), str.length(), spannableString.length(), 17);
        this.messageText.setText(spannableString);
        this.messageText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
