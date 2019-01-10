package com.yc.loanbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.InitInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.engin.InitEngin;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class LoadingActivity extends BaseActivity {

    @BindView(R2.id.active_image)
    ImageView active_image;

    @BindView(R2.id.skip_view)
    TextView skip_view;

    private boolean isGo;

    private int count = 5;

    private ProductInfo init_img;

    private boolean isOpen = true;

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_loading;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData() {
        skip_view.setVisibility(View.VISIBLE);
        Intent intent = getIntent();


        new InitEngin(this).init().subscribe(new Subscriber<ResultInfo<InitInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<InitInfo> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    LoanApplication.getLoanApplication().userInfo = resultInfo.getData().getUserInfo();
                    init_img = resultInfo.getData().getInit_img();
                    GlideHelper.imageView(getBaseContext(), active_image, init_img.getImage(), 0);
                }
            }
        });

        RxView.clicks(skip_view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            nav2MainActivity();
        });

        RxView.clicks(active_image).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            LoanApplication.getLoanApplication().init_img = init_img;
            nav2MainActivity();
        });

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> count-aLong)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        nav2MainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        skip_view.setText("跳过(" + (aLong) + "s)");
                    }
                });
    }


    public void nav2MainActivity(){
        if(!isGo) {
            isGo = true;
            if(isOpen){
                startActivity(new Intent(LoadingActivity.this, LoanboxMainActivity.class));
            } else {
                startActivity(new Intent(LoadingActivity.this, ArticleListActivity.class));
            }
            finish();
        }
    }




}
