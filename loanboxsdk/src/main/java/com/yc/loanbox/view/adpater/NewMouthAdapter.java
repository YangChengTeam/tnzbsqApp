package com.yc.loanbox.view.adpater;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.ScreenUtil;
import com.kk.utils.VUiKit;
import com.yc.loanbox.R;
import com.yc.loanbox.constant.Config;
import com.yc.loanbox.model.bean.ListInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.view.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class NewMouthAdapter extends BaseQuickAdapter<ListInfo, BaseViewHolder> {

    public int type = 0;
    public List<TimeInfo> timeInfos = new ArrayList<>();
    public NewMouthAdapter(List<ListInfo> data, int type) {
        super(R.layout.loanbox_item_new_product_fragment2, data);
        timeInfos.add(new TimeInfo());
        timeInfos.add(new TimeInfo());
        timeInfos.add(new TimeInfo());
        timeInfos.add(new TimeInfo());
        this.type = type;
    }



    @Override
    protected void convert(BaseViewHolder helper, ListInfo item) {

        int i = helper.getAdapterPosition();
        int hour = 0;
        if(i == 0) {
            helper.setText(R.id.time, "10:00场");
            hour = 10;
        } else if(i == 1) {
            helper.setText(R.id.time, "14:00场");
            hour = 14;
        } else if(i == 2) {
            helper.setText(R.id.time, "16:00场");
            hour = 16;
        } else if(i == 3) {
            helper.setText(R.id.time, "20:00场");
            hour = 20;
        }
        if(type == 1){
            hour += 24;
        }
        boolean old = newMouthCountDown(helper, item, i, helper.getView(R.id.hour), helper.getView(R.id.minute), helper.getView(R.id.second), hour, 0 );
        TimeInfo timeInfo = timeInfos.get(i);
        timeInfo.isOnline = !old;

        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        setVisible(helper, old, item);

        RxView.clicks(helper.getView(R.id.open_btn)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            timeInfo.isOpen = !timeInfo.isOpen;
            if(timeInfo.height == 0) {
                timeInfo.height = helper.itemView.getHeight();
            }
            if(timeInfo.isOpen ){
                ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
                layoutParams.height = timeInfo.height;
            } else {
                ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
                layoutParams.height = ScreenUtil.dip2px(mContext, 50);
            }
            setRecyclerView(helper);
        });

        setRecyclerView(helper);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(timeInfo.product2Adapter);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.main_bg)));
        timeInfo.product2Adapter.setOnline(timeInfo.isOnline);
        timeInfo.product2Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(timeInfo.isOnline) {
                    ProductInfo productInfo = (ProductInfo) adapter.getData().get(position);
                    if(productInfo != null){
                        productInfo.setPtype(Config.TYPE107);
                    }
                    ((BaseActivity) mContext).startWebActivity(productInfo);
                } else {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(mContext);
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("新口子还没有上线， 请耐心等候");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    normalDialog.show();
                }
            }
        });
        timeInfo.product2Adapter.setNewData(item.getList());
        timeInfo.product2Adapter.loadMoreEnd();



    }

    private void setVisible(BaseViewHolder helper, boolean old, ListInfo item){
        if(old){
            helper.setVisible(R.id.open_btn, false);
            helper.setVisible(R.id.time_layout, true);
            helper.setVisible(R.id.product_num, false);
        } else {
            helper.setText(R.id.product_num, "已上线" + item.getList().size() + "家");
            helper.setVisible(R.id.open_btn, true);
            helper.setVisible(R.id.time_layout, false);
            helper.setVisible(R.id.product_num, true);
        }
    }


    private void setRecyclerView(BaseViewHolder helper){
        int i = helper.getAdapterPosition();
        if(timeInfos.get(i).isOpen){
            Drawable rightDrawable = mContext.getResources().getDrawable(R.mipmap.ic_blue_arrow_up);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

            helper.setText(R.id.open_btn,  "点击收起");
            ((TextView)helper.getView(R.id.open_btn)).setCompoundDrawables(null, null, rightDrawable, null);
            helper.setVisible(R.id.recyclerView, true);

        } else {
            Drawable rightDrawable = mContext.getResources().getDrawable(R.mipmap.ic_blue_arrow_down);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

            helper.setText(R.id.open_btn,  "点击打开");
            ((TextView)helper.getView(R.id.open_btn)).setCompoundDrawables(null, null, rightDrawable, null);
            helper.setVisible(R.id.recyclerView, false);
        }
    }


    class TimeInfo {
       public int tmpHour;
       public int tmpMinute;
       public int tmpSecond;
       public int height;
       public boolean isOnline = false;
       public boolean isOpen = true;
       public Product2Adapter product2Adapter = new Product2Adapter(null);
    }



    private boolean newMouthCountDown(BaseViewHolder helper, ListInfo item ,int i,  TextView hourTextView,  TextView minuteTextView, TextView secondTextView, int hour, int minute){
        TimeInfo timeInfo = timeInfos.get(i);

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);

        timeInfo.tmpMinute = minute - currentMinute;
        timeInfo.tmpHour = hour - currentHour;

        if(timeInfo.tmpMinute <= 0 && timeInfo.tmpHour-1 < 0){
            return false;
        }

        if(minute <= currentMinute){
            timeInfo.tmpHour -= 1;
            timeInfo.tmpMinute = 59 - currentMinute;
        }

        timeInfo.tmpSecond = 59;

        hourTextView.setText(timeInfo.tmpHour>9 ? timeInfo.tmpHour + "" : "0" + timeInfo.tmpHour);
        minuteTextView.setText(timeInfo.tmpMinute>9 ? timeInfo.tmpMinute + "" : "0" + timeInfo.tmpMinute);
        secondTextView.setText(timeInfo.tmpSecond>9 ? timeInfo.tmpSecond + "" : "0" + timeInfo.tmpSecond);

        final int tmpSeconds = 60 * 60 * timeInfo.tmpHour + 60 * timeInfo.tmpMinute;

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(tmpSeconds)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> tmpSeconds-aLong)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        timeInfo.isOnline = true;
                        timeInfo.product2Adapter.setOnline(timeInfo.isOnline);
                        timeInfo.product2Adapter.notifyDataSetChanged();
                        setVisible(helper, !timeInfo.isOnline, item);
                        timeInfo.tmpHour = 0; timeInfo.tmpMinute = 0; timeInfo.tmpSecond = 0;
                        hourTextView.setText(timeInfo.tmpHour>9 ? timeInfo.tmpHour + "" : "0" + timeInfo.tmpHour);
                        minuteTextView.setText(timeInfo.tmpMinute>9 ? timeInfo.tmpMinute + "" : "0" + timeInfo.tmpMinute);
                        secondTextView.setText(timeInfo.tmpSecond>9 ? timeInfo.tmpSecond + "" : "0" + timeInfo.tmpSecond);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        timeInfo.tmpSecond -= 1;
                        if(timeInfo.tmpSecond == 0){
                            if(timeInfo.tmpMinute > 0 || timeInfo.tmpHour > 0){
                                timeInfo.tmpSecond = 59;
                                if(timeInfo.tmpMinute > 1){
                                    timeInfo.tmpMinute -= 1;
                                } else {
                                    timeInfo.tmpHour -= 1;
                                    timeInfo.tmpMinute = 59;
                                }
                            }
                        }
                        if(timeInfo.tmpHour < 0) {
                            timeInfo.tmpHour = 0;
                        }
                        if(timeInfo.tmpMinute < 0) {
                            timeInfo.tmpMinute = 0;
                        }
                        if(timeInfo.tmpSecond < 0) {
                            timeInfo.tmpSecond = 0;
                        }
                        VUiKit.post(()->{
                            hourTextView.setText(timeInfo.tmpHour>9 ? timeInfo.tmpHour + "" : "0" + timeInfo.tmpHour);
                            minuteTextView.setText(timeInfo.tmpMinute>9 ? timeInfo.tmpMinute + "" : "0" + timeInfo.tmpMinute);
                            secondTextView.setText(timeInfo.tmpSecond>9 ? timeInfo.tmpSecond + "" : "0" + timeInfo.tmpSecond);
                        });
                    }
                });
        return true;
    }

}
