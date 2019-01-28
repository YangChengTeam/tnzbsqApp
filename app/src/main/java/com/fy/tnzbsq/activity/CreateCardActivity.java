package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.CreateCardInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.view.CustomProgress;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.other.ToastUtil;
import com.orhanobut.logger.Logger;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/12/28.
 */

public class CreateCardActivity extends BaseAppActivity {

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.iv_create_bg_iv)
    ImageView mCreateBeforeImageView;

    @BindView(R.id.et_input_name)
    EditText mInputNameEditText;

    @BindView(R.id.btn_create)
    Button mCreateButton;

    OKHttpRequest okHttpRequest = null;

    private CustomProgress dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_card;
    }

    @Override
    protected void initVars() {
        okHttpRequest = new OKHttpRequest();
        mToolbar.setTitle("2018靠啥吃饭");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        RxView.clicks(mCreateButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (StringUtils.isEmpty(mInputNameEditText.getText())) {
                    ToastUtil.toast(CreateCardActivity.this, "请输入你的名字");
                    return;
                }

                if (dialog == null) {
                    dialog = CustomProgress.create(context, "正在生成...", true, null);
                }
                dialog.show();
                createCard(mInputNameEditText.getText().toString());
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public void createCard(String userName) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", userName);
        okHttpRequest.aget(Server.CREATE_LUCK_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (!com.fy.tnzbsq.util.StringUtils.isEmpty(response)) {
                        CreateCardInfo createCardInfo = Contants.gson.fromJson(response, CreateCardInfo.class);
                        if (createCardInfo != null && createCardInfo.errCode.equals("0")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("imagePath", createCardInfo.data);
                            bundle.putString("createTitle", "2018靠啥吃饭");

                            Intent intent = new Intent(CreateCardActivity.this, ResultActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            Toast.makeText(CreateCardActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Logger.e("createCard error");
                    Toast.makeText(CreateCardActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onBefore() {

            }
        });
    }

}
