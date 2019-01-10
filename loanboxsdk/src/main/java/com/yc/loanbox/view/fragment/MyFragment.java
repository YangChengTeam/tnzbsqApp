package com.yc.loanbox.view.fragment;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.yc.loanbox.LoanApplication;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.IndexInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.TypeInfo;
import com.yc.loanbox.view.ListActivity;
import com.yc.loanbox.view.LoanboxMainActivity;
import com.yc.loanbox.view.RecordActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;

public class MyFragment extends BaseFragment {

    @BindView(R2.id.lljl_btn)
    ConstraintLayout lljlLinearLayout;

    @BindView(R2.id.cjwt_btn)
    ConstraintLayout cjwtLinearLayout;

    @BindView(R2.id.swhz_btn)
    ConstraintLayout swhzLinearLayout;

    @BindView(R2.id.nickname)
    TextView nickNameTextView;

    @BindViews({R2.id.type1, R2.id.type2, R2.id.type3, R2.id.type4})
    List<RelativeLayout> mTypesRelativeLayout;

    @BindView(R2.id.version)
    TextView versionTextView;

    private IndexInfo indexInfo;

    public static Fragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_fragment_my;
    }

    @Override
    protected void initViews() {
        indexInfo = LoanboxMainActivity.getLoanboxMainActivity().indexInfo;

        for(int i=0; i < mTypesRelativeLayout.size();i++){
            final  int tmp = i;
            RxView.clicks(mTypesRelativeLayout.get(i)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                if(indexInfo != null && indexInfo.getType_list() != null) {
                    intent.putExtra("type_id", indexInfo.getType_list().get(tmp).getId());
                    intent.putExtra("name", indexInfo.getType_list().get(tmp).getName());
                }
                startActivity(intent);
            });
        }

        if(indexInfo != null && indexInfo.getType_list() != null) {
            for (int i = 0; i < indexInfo.getType_list().size(); i++) {
                TypeInfo typeInfo = indexInfo.getType_list().get(i);
                if (i < mTypesRelativeLayout.size()) {
                    ImageView imageView = (ImageView) mTypesRelativeLayout.get(i).getChildAt(0);
                    TextView textView = (TextView) mTypesRelativeLayout.get(i).getChildAt(1);

                    GlideHelper.imageView(getActivity(), imageView, typeInfo.getIco(), 0);
                    textView.setText(typeInfo.getName());
                }
            }
        }

        RxView.clicks(lljlLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            startActivity(new Intent(getActivity(), RecordActivity.class));
        });

        RxView.clicks(cjwtLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setName("常见问题");
            productInfo.setReg_url("http://u.wk990.com/loan_html/question.html");
            startWebActivity(productInfo);
        });

        RxView.clicks(swhzLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setName("商务合作");
            productInfo.setReg_url("http://u.wk990.com/loan_html/joint-work.html");
            startWebActivity(productInfo);
        });

        versionTextView.setText("版本号:"+GoagalInfo.get().packageInfo.versionName);
    }

    @Override
    protected void initData() {
        super.initData();
        if(LoanApplication.getLoanApplication().userInfo != null) {
            nickNameTextView.setText("小林_" + LoanApplication.getLoanApplication().userInfo.getUser_id());
        }
    }
}
