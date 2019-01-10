package com.yc.loanbox.view.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ExchangeGalleryListEvent;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.engin.LikeEngin;
import com.yc.loanbox.view.adpater.GalleryAdapter;
import com.yc.loanbox.view.adpater.OnRecyclerViewListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class GalleryFragment extends BaseFragment {

    private GalleryAdapter adapter;

    @BindView(R2.id.loading_fail_layout)
    LinearLayout loading_fail_layout;
    @BindView(R2.id.loading_layout)
    LinearLayout loading_layout;

    @BindView(R2.id.vp)
    ViewPager f7468vp;

    @BindView(R2.id.type_name)
    TextView tv_type_name;

    private LikeEngin likeEngin;

    private List<ProductInfo> productInfos;

    private int exchange_index = 0;


    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_gallery;
    }

    @Override
    protected void initViews() {
        this.tv_type_name.setText(getString(R.string.main_tab3_text));
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    private void loadData(){
        this.showLoadingDialog( "加载中...");
        likeEngin = new LikeEngin(getActivity());
        Subscription subscription = likeEngin.getLikeInfo().subscribe(new Subscriber<ResultInfo<List<ProductInfo>>>() {
            @Override
            public void onCompleted() {
                dissmissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                dissmissLoadingDialog();
                GalleryFragment.this.f7468vp.setVisibility(View.GONE);
                GalleryFragment.this.loading_layout.setVisibility(View.GONE);
                GalleryFragment.this.loading_fail_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(final ResultInfo<List<ProductInfo>> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    productInfos = resultInfo.getData();
                    GalleryFragment.this.f7468vp.setVisibility(View.VISIBLE);
                    GalleryFragment.this.loading_layout.setVisibility(View.GONE);
                    GalleryFragment.this.loading_fail_layout.setVisibility(View.GONE);
                    GalleryFragment.this.adapter = new GalleryAdapter(GalleryFragment.this.mContext, resultInfo.getData());
                    GalleryFragment.this.f7468vp.setAdapter(GalleryFragment.this.adapter);
                    GalleryFragment.this.f7468vp.setOffscreenPageLimit(3);
                    GalleryFragment.this.f7468vp.setPageTransformer(false, new ViewPager.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View view, float position) {
                            if (position < -1.0f) {
                                position = -1.0f;
                            } else if (position > 1.0f) {
                                position = 1.0f;
                            }
                            float scaleValue = 0.9f + (0.1f * (position < 0.0f ? 1.0f + position : 1.0f - position));
                            view.setScaleX(scaleValue);
                            view.setScaleY(scaleValue);
                        }
                    });
                    GalleryFragment.this.adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            ProductInfo productInfo = productInfos.get(i);
                            if(productInfo != null){
                                productInfo.setPtype(Config.TYPE103);
                            }
                            startWebActivity(productInfo);
                        }

                        @Override
                        public boolean onItemLongClick(View view, int i) {
                            return false;
                        }
                    });

                }
            }
        });
        mSubscriptions.add(subscription);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exchangeList(ExchangeGalleryListEvent event) {
        if (this.adapter != null) {
            this.exchange_index++;
            if (this.exchange_index >= this.adapter.getCount()) {
                this.exchange_index = 0;
            }
            this.f7468vp.setCurrentItem(this.exchange_index);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
