package com.yc.loanboxsdk;

import android.content.Context;
import android.content.Intent;

import com.kk.securityhttp.domain.ResultInfo;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.model.bean.InitInfo;
import com.yc.loanbox.model.engin.InitEngin;
import com.yc.loanbox.view.LoanboxMainActivity;

import rx.Subscriber;

public class LoanboxSDK {
    public static LoanboxSDK loanboxSDK;

    private boolean isLogin;
    private Context mContext;

    public static LoanboxSDK defaultLoanboxSDK(){
        if(loanboxSDK == null){
            loanboxSDK = new LoanboxSDK();
        }
        return loanboxSDK;
    }

    public void init(Context context){
        this.mContext = context;
        LoanApplication.getLoanApplication().inits(mContext);
        new InitEngin(context).init().subscribe(new Subscriber<ResultInfo<InitInfo>>() {
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
                }
            }
        });
    }

    public void setChannelId(String agentId){
        LoanApplication.getLoanApplication().agent_id = agentId;
    }

    public void setPhone(String phone){
        MMKV.defaultMMKV().putString("phone", phone);
    }

    public void setNeedLogin(boolean login) {
        this.isLogin = login;
    }

    public void open(){
        Intent intent = new Intent(mContext, LoanboxMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public boolean isLogin() {
        return isLogin;
    }
}
