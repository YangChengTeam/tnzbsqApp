package com.yc.loanbox.view.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.RefreshTab2Event;
import com.yc.loanbox.model.bean.SortInfo;
import com.yc.loanbox.model.bean.SortsInfo;
import com.yc.loanbox.model.engin.SearchEngin;
import com.yc.loanbox.model.engin.SortEngin;
import com.yc.loanbox.view.BaseActivity;
import com.yc.loanbox.view.adpater.ProductAdapter;
import com.yc.loanbox.view.widget.DrawableCenterTextView;
import com.yc.loanbox.view.widget.WarpLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import rx.Subscriber;
import rx.Subscription;

public class ProductGridFragment extends BaseFragment {

    private String money_min;
    private String money_max;
    private String time_min;
    private String time_max;
    private String class_id;

    private List<SortInfo> sortModels1 = new ArrayList();
    private List<SortInfo> sortModels2 = new ArrayList();
    private List<SortInfo> sortModels3 = new ArrayList();

    List<TextView> popTextViewList1 = new ArrayList();
    List<TextView> popTextViewList2 = new ArrayList();
    List<TextView> popTextViewList3 = new ArrayList();

    private boolean isChooseShaiXuan1 = false;
    private boolean isChooseShaiXuan2 = false;
    private boolean isChooseShaiXuan3 = false;

    private String sort_choose_flag_url = "";
    private String sort_choose_type = "";

    private boolean isOK = false;


    private boolean isShowOk = true;

    private String pop_string1 = "借款金额";
    private String pop_string2 = "借款时间";
    private String pop_string3 = "借款类型";

    private String pop_flag1 = "";
    private String pop_flag2 = "";
    private String pop_flag3 = "";

    private SortEngin sortEngin;

    @BindView(R2.id.sort_layout)
    LinearLayout sort_layout;


    @BindViews({R2.id.sort1_btn, R2.id.sort2_btn, R2.id.sort3_btn})
    List<TextView> sortsTextView;

    @BindView(R2.id.recyclerView)
    RecyclerView mProductRecyclerView;

    private ProductAdapter productAdapter;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R2.id.message)
    TextView messageText;

    @BindView(R2.id.sort4_btn)
    DrawableCenterTextView sort4_btn;

    @BindView(R2.id.type_name)
    TextView tv_type_name;

    private List<ProductInfo> productInfos;

    private SearchEngin searchEngin;

    private PopupWindow mPop;


    public static Fragment newInstance() {
        return new ProductGridFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_product_grid;
    }

    @Override
    protected void initViews() {
        this.tv_type_name.setText(getString(R.string.main_tab2_text));

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
                ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.type_name_color));
                setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
                ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.type_name_color));
                sortsTextView.get(0).setTextColor(getResources().getColor(R.color.type_name_color));
                sortsTextView.get(1).setTextColor(getResources().getColor(R.color.type_name_color));
                sortsTextView.get(2).setTextColor(getResources().getColor(R.color.type_name_color));
                loadData("", "", "", "", "");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(R.layout.loanbox_item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                setMessage(productInfo.getName());
                if(productInfo != null){
                    productInfo.setPtype(Config.TYPE102);
                }
                ((BaseActivity) getActivity()).startWebActivity(productInfo);
            }
        });

        RxView.clicks(sort4_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view -> {
            showLookArticleTipPop();
        });

        for (TextView sort_btn : sortsTextView
                ) {
            RxView.clicks(sort_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view -> {
                loadData("", "", "", "", "");
                setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
                ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.type_name_color));

                this.sortsTextView.get(0).setTextColor(getResources().getColor(R.color.type_name_color));
                this.sortsTextView.get(1).setTextColor(getResources().getColor(R.color.type_name_color));
                this.sortsTextView.get(2).setTextColor(getResources().getColor(R.color.type_name_color));
                sort_btn.setTextColor(getResources().getColor(R.color.colorAccent));

            });
        }
    }


    private void showLookArticleTipPop() {

        if (this.mPop == null) {
            View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loanbox_pop_sort, null);
            this.mPop = new PopupWindow(view, -1, -1);
            this.mPop.setFocusable(false);
            this.mPop.setOutsideTouchable(true);
            this.mPop.setAnimationStyle(0);
            this.mPop.setBackgroundDrawable(new BitmapDrawable());
            this.mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (!isOK) {
                        setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
                        ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.type_name_color));
                    }
                    isOK = false;
                }
            });

            TextView name2 = (TextView) view.findViewById(R.id.name2);
            TextView name3 = (TextView) view.findViewById(R.id.name3);
            ((TextView) view.findViewById(R.id.name1)).setText(this.pop_string1);
            name2.setText(this.pop_string2);
            name3.setText(this.pop_string3);
            WarpLinearLayout layou2 = (WarpLinearLayout) view.findViewById(R.id.type_list2);
            WarpLinearLayout layou3 = (WarpLinearLayout) view.findViewById(R.id.type_list3);
            addPopTypeList((WarpLinearLayout) view.findViewById(R.id.type_list1), this.sortModels1, 1);
            addPopTypeList(layou2, this.sortModels2, 2);
            addPopTypeList(layou3, this.sortModels3, 3);
            view.findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ProductGridFragment.this.mPop.dismiss();
                    }catch (Exception e){}
                }
            });
            view.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductGridFragment.this.sort_choose_flag_url = "";
                    ProductGridFragment.this.setPopText(0, ProductGridFragment.this.popTextViewList1);
                    ProductGridFragment.this.setPopText(0, ProductGridFragment.this.popTextViewList2);
                    ProductGridFragment.this.setPopText(0, ProductGridFragment.this.popTextViewList3);
                }
            });
            view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isOK = true;

                    setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan2, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
                    ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.colorAccent));

                    sortsTextView.get(0).setTextColor(getResources().getColor(R.color.type_name_color));
                    sortsTextView.get(1).setTextColor(getResources().getColor(R.color.type_name_color));
                    sortsTextView.get(2).setTextColor(getResources().getColor(R.color.type_name_color));
                    try {
                        ProductGridFragment.this.mPop.dismiss();
                    }catch (Exception e){}
                    loadData();
                }
            });
        }
        if (this.isShowOk) {
            ProductGridFragment.showAsDropDown(this.mPop, this.sort_layout, 0, 0);
        }
    }

    public static void showAsDropDown(PopupWindow pw, View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            pw.setHeight(anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom);
            pw.showAsDropDown(anchor, xoff, yoff);
            return;
        }
        pw.showAsDropDown(anchor, xoff, yoff);
    }


    private void addPopTypeList(WarpLinearLayout layout, List<SortInfo> list, int type) {
        int size = list.size();
        int px_10 = ScreenUtil.dip2px(getActivity(), 10.0f);
        int px_5 = ScreenUtil.dip2px(getActivity(), 5.0f);
        int px_8 = ScreenUtil.dip2px(getActivity(), 8.0f);
        int w = (ScreenUtil.getWidth(getActivity()) - ScreenUtil.dip2px(getActivity(), 40.0f)) / 3;
        int i = 0;
        while (i < size) {
            final int index = i;
            final SortInfo model = (SortInfo) list.get(i);
            FrameLayout l = new FrameLayout(getActivity());
            TextView textView = new TextView(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, -2);
            if (i == 1 || i == 4 || i == 7) {
                params.setMargins(px_10, px_5, px_10, px_5);
            } else {
                params.setMargins(0, px_5, 0, px_5);
            }
            textView.setLayoutParams(params);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setBackgroundResource(R.drawable.accent_kuang_30dp_yuanjiao_sty);
            } else {
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setBackgroundResource(R.drawable.grey_kuang_30dp_yuanjiao_sty);
            }
            textView.setGravity(17);
            textView.setTextSize(13.0f);
            textView.setPadding(0, px_8, 0, px_8);
            textView.setText(model.getName());
            final int i2 = type;
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (i2 == 1) {
                        money_min = model.getValue_min();
                        money_max = model.getValue_max();
                        LogUtil.msg(money_max+"-"+ money_min);
                        ProductGridFragment.this.sort_choose_flag_url = ProductGridFragment.this.sort_choose_flag_url + "&" + ProductGridFragment.this.pop_flag1 + "=" + ProductGridFragment.this.pop_flag1 + model.getId();
                        ProductGridFragment.this.setPopText(index, ProductGridFragment.this.popTextViewList1);
                        if (TextUtils.equals(model.getId(), "1")) {
                            ProductGridFragment.this.isChooseShaiXuan1 = false;
                        } else {
                            ProductGridFragment.this.isChooseShaiXuan1 = true;
                        }
                    } else if (i2 == 2) {
                        time_min = model.getValue_min();
                        time_max = model.getValue_max();
                        ProductGridFragment.this.sort_choose_flag_url = ProductGridFragment.this.sort_choose_flag_url + "&" + ProductGridFragment.this.pop_flag2 + "=" + ProductGridFragment.this.pop_flag2 + model.getId();
                        ProductGridFragment.this.setPopText(index, ProductGridFragment.this.popTextViewList2);
                        if (TextUtils.equals(model.getId(), "1")) {
                            ProductGridFragment.this.isChooseShaiXuan2 = false;
                        } else {
                            ProductGridFragment.this.isChooseShaiXuan2 = true;
                        }
                    } else if (i2 == 3) {
                        class_id = model.getId();
                        ProductGridFragment.this.sort_choose_flag_url = ProductGridFragment.this.sort_choose_flag_url + "&" + ProductGridFragment.this.pop_flag3 + "=" + ProductGridFragment.this.pop_flag3 + model.getId();
                        ProductGridFragment.this.setPopText(index, ProductGridFragment.this.popTextViewList3);
                        if (TextUtils.equals(model.getId(), "1")) {
                            ProductGridFragment.this.isChooseShaiXuan3 = false;
                        } else {
                            ProductGridFragment.this.isChooseShaiXuan3 = true;
                        }
                    }
                }
            });
            if (type == 1) {
                this.popTextViewList1.add(textView);
            } else if (type == 2) {
                this.popTextViewList2.add(textView);
            } else if (type == 3) {
                this.popTextViewList3.add(textView);
            }
            l.addView(textView);
            layout.addView(l);
            i++;
        }
    }

    public void setTextImage(Context context, int res, TextView textView, TextImage textImage) {
        Drawable d = context.getResources().getDrawable(res);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        switch (textImage) {
            case LEFT:
                textView.setCompoundDrawables(d, null, null, null);
                return;
            case RIGHT:
                textView.setCompoundDrawables(null, null, d, null);
                return;
            case TOP:
                textView.setCompoundDrawables(null, d, null, null);
                return;
            case BOTTOM:
                textView.setCompoundDrawables(null, null, null, d);
                return;
            default:
                return;
        }
    }


    public enum TextImage {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    private void setPopText(int index, List<TextView> list) {
        for (int j = 0; j < list.size(); j++) {
            if (index == j) {
                ((TextView) list.get(j)).setTextColor(getResources().getColor(R.color.colorAccent));
                ((TextView) list.get(j)).setBackgroundResource(R.drawable.accent_kuang_30dp_yuanjiao_sty);
            } else {
                ((TextView) list.get(j)).setTextColor(getResources().getColor(R.color.black));
                ((TextView) list.get(j)).setBackgroundResource(R.drawable.grey_kuang_30dp_yuanjiao_sty);
            }
        }
    }


    @Override
    protected void initData() {
        SortInfo sortInfo = new SortInfo();
        sortInfo.setName("无限");
        sortInfo.setValue_min("");
        sortInfo.setValue_max("");
        sortInfo.setId("");
        sortModels1.add(sortInfo);
        sortModels2.add(sortInfo);
        sortModels3.add(sortInfo);
        searchEngin = new SearchEngin(getActivity());
        sortEngin = new SortEngin(getActivity());
        sortEngin.getSortInfo().subscribe(new Subscriber<ResultInfo<SortsInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<SortsInfo> resultInfo) {
                if (resultInfo.getCode() == 1) {
                    sortModels1.addAll(resultInfo.getData().getMoney()) ;
                    sortModels2.addAll(resultInfo.getData().getTime());
                    sortModels3.addAll(resultInfo.getData().getClass2());
                }
            }
        });
        loadData("", "", "", "", "");
    }

    private void loadData() {
        loadData(money_min, money_max, time_min, time_max, class_id);
    }

    private void loadData(String money_min, String money_max, String time_min, String time_max, String class_id) {
        ProductGridFragment.this.showLoadingDialog("加载中...");
        Subscription subscription = searchEngin.getSearchInfo(money_min, money_max, time_min, time_max, class_id).subscribe(new Subscriber<ResultInfo<List<ProductInfo>>>() {
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
                if (resultInfo.getCode() == 1) {
                    ProductGridFragment.this.productInfos = resultInfo.getData();
                    setMessage("");
                    productAdapter.setNewData(resultInfo.getData());
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    class MyClickableSpan extends ClickableSpan {
        private ProductInfo productInfo;

        public MyClickableSpan(ProductInfo productInfo) {
            this.productInfo = productInfo;
        }

        public void onClick(View widget) {
            if(productInfo != null){
                productInfo.setPtype(Config.TYPE98);
            }
            startWebActivity(productInfo);
        }
    }


    private void setMessage(String click_app_name) {
        if (productInfos == null || productInfos.size() < 2) return;

        ProductInfo productInfo;
        String tmp_click_app_name = MMKV.defaultMMKV().getString("click_app_name", "");
        String next_click_app_name = "";
        int tmpIndx = 0;
        if (TextUtils.isEmpty(click_app_name)) {
            if (!TextUtils.isEmpty(click_app_name)) {
                click_app_name = tmp_click_app_name;
            } else {
                tmpIndx = new Random().nextInt(productInfos.size());
                click_app_name = productInfos.get(tmpIndx).getName();
                MMKV.defaultMMKV().putString("click_app_name", click_app_name);
            }
        }
        int index = new Random().nextInt(productInfos.size());
        productInfo = productInfos.get(index);
        next_click_app_name = productInfo.getName();

        if (next_click_app_name.equals(click_app_name) || tmpIndx == index) {
            if (index > 0) {
                index -= 1;
            }
            if (index == 0) {
                index += 1;
            }
            productInfo = productInfos.get(index);
            next_click_app_name = productInfo.getName();
        }
        String str = "申请\"" + click_app_name + "\"的用户同时还申请了";
        SpannableString spannableString = new SpannableString(str + next_click_app_name);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), str.length(), spannableString.length(), 17);
        spannableString.setSpan(new UnderlineSpan(), str.length(), spannableString.length(), 17);
        spannableString.setSpan(new MyClickableSpan(productInfo), str.length(), spannableString.length(), 17);
        this.messageText.setText(spannableString);
        this.messageText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshTab2Event event) {
        if (this.mPop != null) {
            try {
                ProductGridFragment.this.mPop.dismiss();
            }catch (Exception e){}
        }
        setTextImage(ProductGridFragment.this.getActivity(), R.mipmap.ic_shaixuan, ProductGridFragment.this.sort4_btn, TextImage.RIGHT);
        ProductGridFragment.this.sort4_btn.setTextColor(ProductGridFragment.this.getResources().getColor(R.color.type_name_color));
        sortsTextView.get(0).setTextColor(getResources().getColor(R.color.type_name_color));
        sortsTextView.get(1).setTextColor(getResources().getColor(R.color.type_name_color));
        sortsTextView.get(2).setTextColor(getResources().getColor(R.color.type_name_color));
        loadData("", "", "", "", "");
    }

    ;

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
