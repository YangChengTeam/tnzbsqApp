package com.yc.loanbox.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ScreenUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.helper.BannerImageLoader;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.CodeInfo;
import com.yc.loanbox.model.bean.IndexInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.RefreshTab1Event;
import com.yc.loanbox.model.bean.RollInfo;
import com.yc.loanbox.model.bean.TypeInfo;
import com.yc.loanbox.model.bean.UserInfo;
import com.yc.loanbox.model.engin.IndexEngin;
import com.yc.loanbox.model.engin.LoginEngin;
import com.yc.loanbox.model.engin.SmsEngin;
import com.yc.loanbox.view.BaseActivity;
import com.yc.loanbox.view.ListActivity;
import com.yc.loanbox.view.LoanboxMainActivity;
import com.yc.loanbox.view.NewMouthActivity;
import com.yc.loanbox.view.adpater.ProductAdapter;
import com.yc.loanbox.view.widget.ActiveDialog;
import com.yc.loanbox.view.widget.LoginDialog;
import com.yc.loanboxsdk.LoanboxSDK;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class IndexFragment extends BaseFragment {

    @BindView(R2.id.scrollView)
    ScrollView scrollView;

    @BindView(R2.id.banner)
    Banner mBanner;

    @BindView(R2.id.recyclerView)
    RecyclerView mProductRecyclerView;

    @BindViews({R2.id.type1, R2.id.type2, R2.id.type3, R2.id.type4})
    List<RelativeLayout> mTypesRelativeLayout;

    @BindViews({R2.id.type11, R2.id.type22, R2.id.type33, R2.id.type44})
    List<RelativeLayout> mTypesRelativeLayout2;

    @BindView(R2.id.view_flipper)
    ViewFlipper article_flipper;

    @BindView(R2.id.sfz_btn)
    ImageView sfzImageView;

    @BindView(R2.id.zmf_btn)
    ImageView zmfImageView;

    @BindView(R2.id.more_btn)
    TextView moreTextView;

    @BindView(R2.id.tv_switch)
    TextView switchTextView;

    @BindView(R2.id.arrow_anim_image1)
    ImageView animImage;

    @BindView(R2.id.arrow_anim_image2)
    ImageView animImage2;

    @BindView(R2.id.hour)
    TextView hourTextView;

    @BindView(R2.id.minute)
    TextView minuteTextView;

    @BindView(R2.id.second)
    TextView secondTextView;

    @BindView(R2.id.new_mouth_count)
    TextView newMouthCountTextView;

    @BindView(R2.id.new_product_btn)
    LinearLayout newMouthBtn;

    private String user_id;

    private ProductAdapter productAdapter;

    private LoginDialog loginDialog;
    private IndexInfo indexInfo;

    private IndexEngin indexEngin;

    private LoginEngin loginEngin;
    private SmsEngin smsEngin;

    private ActiveDialog activeDialog;

    private int tmpMinute = 0;
    private int tmpHour = 0;
    private int tmpSecond = 0;

    public static Fragment newInstance() {
        return new IndexFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_index;
    }

    @Override
    protected void initViews() {
        activeDialog = new ActiveDialog(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(R.layout.loanbox_item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                productInfo.setPtype(Config.TYPE101);
                ((BaseActivity)getActivity()).startWebActivity(productInfo);
            }
        });

        mBanner.setFocusable(false);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ((BaseActivity)getActivity()).startWebActivity(indexInfo.getBanner_list().get(position));
            }
        });

        for(int i=0; i < mTypesRelativeLayout.size();i++){
            final  int tmp = i;
            RxView.clicks(mTypesRelativeLayout.get(i)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                 if(indexInfo != null && indexInfo.getType_list() != null) {
                     Intent intent = new Intent(getActivity(), ListActivity.class);
                     intent.putExtra("type_id", indexInfo.getType_list().get(tmp).getId());
                     intent.putExtra("name", indexInfo.getType_list().get(tmp).getName());
                     startActivity(intent);
                 }
            });
        }

        RxView.clicks(switchTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            randomYouLike();
        });

        RxView.clicks(sfzImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            ((BaseActivity)getActivity()).startWebActivity(indexInfo.getAd_left_info());
        });

        RxView.clicks(zmfImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            ((BaseActivity)getActivity()).startWebActivity(indexInfo.getAd_right_info());
        });

        RxView.clicks(moreTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                LoanboxMainActivity.getLoanboxMainActivity().showFragment(2);
        });

        RxView.clicks(newMouthBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                startActivity(new Intent(getActivity(), NewMouthActivity.class));
        });

        user_id = MMKV.defaultMMKV().getString("phone", "");
        if(TextUtils.isEmpty(user_id) && LoanboxSDK.defaultLoanboxSDK().isLogin()){
            showLoginDialog();
        } else {
            showLoadingDialog("加载中...");
        }

        VUiKit.postDelayed(1000, new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(animImage,"translationX",0, ScreenUtil.dip2px(getContext(), 20));
                animator.setRepeatCount(ObjectAnimator.INFINITE);

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(animImage2,"translationX",0, ScreenUtil.dip2px(getContext(), 12));
                animator2.setRepeatCount(ObjectAnimator.INFINITE);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1500);
                animatorSet.play(animator).after(7500).with(animator2);
                animatorSet.start();
                newMouthCountDown();
            }
        });
    }

    private void newMouthCountDown(){
        int hour; int minute = 0;
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);

        if(currentHour > 20) {return;
        }
        else if(currentHour >= 16) {
            hour = 20;
        }
        else if(currentHour >= 14) {
            hour = 16;
        }
        else if(currentHour >= 10) {
            hour = 14;
        }
        else {
            hour = 10;
        }

        tmpMinute = minute - currentMinute;
        tmpHour = hour - currentHour;

        if(tmpMinute <= 0 && tmpHour-1 < 0){
            return;
        }

        if(minute <= currentMinute){
            tmpHour -= 1;
            tmpMinute = 59 - currentMinute;
        }

        tmpSecond = 59;
        hourTextView.setText(tmpHour>9 ? tmpHour + "" : "0" + tmpHour);
        minuteTextView.setText(tmpMinute>9 ? tmpMinute + "" : "0" + tmpMinute);
        secondTextView.setText(tmpSecond>9 ? tmpSecond + "" : "0" + tmpSecond);

        final int tmpSeconds = 60 * 60 * tmpHour + 60 * tmpMinute;

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(tmpSeconds)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> tmpSeconds-aLong)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        tmpHour = 0; tmpMinute = 0; tmpSecond = 0;
                        hourTextView.setText(tmpHour>9 ? tmpHour + "" : "0" + tmpHour);
                        minuteTextView.setText(tmpMinute>9 ? tmpMinute + "" : "0" + tmpMinute);
                        secondTextView.setText(tmpSecond>9 ? tmpSecond + "" : "0" + tmpSecond);
                        if(currentHour < 20) {
                            newMouthCountDown();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tmpSecond -= 1;
                        if(tmpSecond == 0){
                            if(tmpMinute > 0 || tmpHour > 0){
                                tmpSecond = 59;
                                if(tmpMinute > 1){
                                    tmpMinute -= 1;
                                } else {
                                    tmpHour -= 1;
                                    tmpMinute = 59;
                                }
                            }
                        }
                        VUiKit.post(()->{
                            if(tmpHour < 0) {
                                tmpHour = 0;
                            }
                            if(tmpMinute < 0) {
                                tmpMinute = 0;
                            }
                            if(tmpSecond < 0) {
                                tmpSecond = 0;
                            }
                            hourTextView.setText(tmpHour>9 ? tmpHour + "" : "0" + tmpHour);
                            minuteTextView.setText(tmpMinute>9 ? tmpMinute + "" : "0" + tmpMinute);
                            secondTextView.setText(tmpSecond>9 ? tmpSecond + "" : "0" + tmpSecond);
                        });
                    }
                });
    }

    private void randomYouLike(){
        int logoImageWidth = ScreenUtil.getWidth(mContext) / 8;

        if(indexInfo != null && indexInfo.getYoulike_list()!= null){
            Collections.shuffle(indexInfo.getYoulike_list());
            int randomSeriesLength = 4;
            List<ProductInfo> randomSeries = indexInfo.getYoulike_list().subList(0, randomSeriesLength);
            for(int i =0; i < randomSeries.size(); i++){
                ProductInfo productInfo = indexInfo.getYoulike_list().get(i);
                if(i < mTypesRelativeLayout2.size()){
                    ImageView imageView =  (ImageView)mTypesRelativeLayout2.get(i).getChildAt(0);
                    TextView textView = (TextView)mTypesRelativeLayout2.get(i).getChildAt(1);

                    GlideHelper.circleBorderImageView(mContext, imageView, productInfo.getIco(), R.mipmap.product_default_image,0, 0, logoImageWidth, logoImageWidth);
                    textView.setText(productInfo.getName());

                    RxView.clicks(mTypesRelativeLayout2.get(i)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                        if(productInfo != null) {
                            productInfo.setPtype(Config.TYPE100);
                        }
                        ((BaseActivity)getActivity()).startWebActivity(productInfo);
                    });
                }
            }
        }
    }

    public void set_message_list(List<RollInfo> list) {
        article_flipper.removeAllViews();
        for (final RollInfo rollInfo : list) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.loanbox_view_scroll, null);
            (view.findViewById(R.id.layout)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ProductInfo productInfo = rollInfo.getLoan_info();
                    if(productInfo != null) {
                        productInfo.setPtype(Config.TYPE99);
                    }
                    startWebActivity(productInfo);
                }
            });
            ((TextView) view.findViewById(R.id.text)).setText(Html.fromHtml(rollInfo.getRoll_msg()));
            article_flipper.addView(view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshTab1Event event) {
        showLoadingDialog("加载中...");
        loadData();
    };


    @Override
    protected void initData() {
        loginEngin = new LoginEngin(getActivity());
        smsEngin = new SmsEngin(getActivity());
        indexEngin = new IndexEngin(getActivity());
        loadData();
    }

    private void loadData(){
        Subscription subscription =  indexEngin.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfo>>() {
            @Override
            public void onCompleted() {
                scrollView.setVisibility(View.VISIBLE);
                dissmissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                dissmissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<IndexInfo> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    indexInfo = resultInfo.getData();
                    LoanboxMainActivity.getLoanboxMainActivity().indexInfo = indexInfo;
                    List<String> bannerImages = new ArrayList<>();
                    for (ProductInfo product: indexInfo.getBanner_list()
                            ) {
                        bannerImages.add(product.getImage());
                    }
                    showBannerImages(bannerImages);
                    set_message_list(resultInfo.getData().getApp_msg_list());
                    productAdapter.setNewData(resultInfo.getData().getHot_list());

                    if(!TextUtils.isEmpty(user_id) && indexInfo.getOpen_window_ad() != null){
                        activeDialog.setImageUrl(indexInfo.getOpen_window_ad().getIco());
                        activeDialog.show();
                        RxView.clicks(activeDialog.activeImage).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                             LoanboxMainActivity.getLoanboxMainActivity().startWebActivity(indexInfo.getOpen_window_ad());
                        });
                    }

                    for(int i =0; i <indexInfo.getType_list().size(); i++){
                        TypeInfo typeInfo = indexInfo.getType_list().get(i);
                        if(i < mTypesRelativeLayout.size()){
                            ImageView imageView =  (ImageView)mTypesRelativeLayout.get(i).getChildAt(0);
                            TextView textView = (TextView)mTypesRelativeLayout.get(i).getChildAt(1);

                            GlideHelper.imageView(mContext, imageView, typeInfo.getIco(), 0);
                            textView.setText(typeInfo.getName());
                        }
                    }
                    randomYouLike();

                    if(indexInfo.getAd_left_info() != null) {
                        GlideHelper.imageView(mContext, sfzImageView, indexInfo.getAd_left_info().getAd_ico(), 0);
                    }

                    if(indexInfo.getAd_right_info() != null) {
                        GlideHelper.imageView(mContext, zmfImageView, indexInfo.getAd_right_info().getAd_ico(), 0);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void showBannerImages(List<String> images){

        mBanner.isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }

    private void showLoginDialog(){
        loginDialog = new LoginDialog(getActivity());
        loginDialog.yzmTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginDialog.yzmTextView.setEnabled(false);
                String phone = loginDialog.phoneEditText.getText().toString();
                if (phone.length() == 11) {
                    sendSms(phone);
                    countDown();
                } else {
                    loginDialog.yzmTextView.setEnabled(true);
                    ToastUtil.toast2(getActivity(), "手机号码有误!");
                }
            }
        });

        loginDialog.loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone = loginDialog.phoneEditText.getText().toString();
                String code = loginDialog.yzmEditText.getText().toString();
                if (phone.length() == 11 && code.length() == 4) {
                    login(phone, code);
                } else {
                    ToastUtil.toast2(getActivity(), "手机号码或验证码有误!");
                }
            }
        });

        loginDialog.show();

    }

    private void login(String phone, String code){
        showLoadingDialog("登录中...");
        loginEngin.login(phone, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                dissmissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                dissmissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<UserInfo> resultInfo) {
                if(resultInfo.getCode() == 1) {
                   UserInfo userInfo = LoanApplication.getLoanApplication().userInfo;
                   if(userInfo != null) {
                       userInfo.setMobile(phone);
                       MMKV.defaultMMKV().putString("phone", phone);
                       loginDialog.dismiss();
                       if(indexInfo != null && indexInfo.getOpen_window_ad() != null){
                           activeDialog.setImageUrl(indexInfo.getOpen_window_ad().getIco());
                           activeDialog.show();
                           RxView.clicks(activeDialog.activeImage).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                               LoanboxMainActivity.getLoanboxMainActivity().startWebActivity(indexInfo.getOpen_window_ad());
                           });
                       }
                   }
                } else {
                    ToastUtil.toast2(getActivity(), resultInfo.getMsg()+"");
                }
            }
        });
    }

    private void sendSms(String phone){
        smsEngin.sendSms(phone).subscribe(new Subscriber<ResultInfo<CodeInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<CodeInfo> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    ToastUtil.toast2(getActivity(), "验证码已经成功发送,请注意查收");
                } else {
                    ToastUtil.toast2(getActivity(), resultInfo.getMsg()+"");
                }
            }
        });
    }

    private int count = 59;
    private void countDown(){
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> count-aLong)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        loginDialog.yzmTextView.setEnabled(true);
                        loginDialog.yzmTextView.setTextColor(getResources().getColor(R.color.blue_text_color));
                        loginDialog.yzmTextView.setText("重新发送");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        loginDialog.yzmTextView.setText((aLong) + "秒后重新获取");
                        loginDialog.yzmTextView.setTextColor(getResources().getColor(R.color.grey_text_color));

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
        EventBus.getDefault().unregister(this);
    }

}
