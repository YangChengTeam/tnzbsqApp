package com.yc.loanbox.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yc.loanbox.R;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.view.BaseActivity;
import com.yc.loanbox.view.widget.MyLoadingDialog;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    public MyLoadingDialog dlg = null;

    protected CompositeSubscription mSubscriptions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), null);
            ButterKnife.bind(this, mRootView);
            if(mRootView.findViewById(R.id.tc_view) != null){
                setTcView(mRootView.findViewById(R.id.tc_view));
            }
            mSubscriptions = new CompositeSubscription();
            dlg = new MyLoadingDialog(this.getContext());
            initViews();
            initData();
            mContext = getActivity();
        }
        return mRootView;
    }

    public void startWebActivity(ProductInfo productInfo){
        ((BaseActivity)getActivity()).startWebActivity(productInfo);
    }

    public void showLoadingDialog(String message) {
        dlg.show(message);
    }

    public void dissmissLoadingDialog(){
        dlg.dismiss();
    }


    protected abstract int  getLayoutId();

    protected abstract void initViews();

    protected void initData(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    public void setTcView(View v) {
        int statusBarHeight1 = 50;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        v.setLayoutParams(new LinearLayout.LayoutParams(-1, statusBarHeight1));
    }




}
