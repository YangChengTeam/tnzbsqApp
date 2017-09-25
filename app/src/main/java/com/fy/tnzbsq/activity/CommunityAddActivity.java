package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ImageSelectedAdapter;
import com.fy.tnzbsq.bean.CommunityInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.ImageUtil;
import com.fy.tnzbsq.view.CustomProgress;
import com.hwangjr.rxbus.RxBus;
import com.kk.utils.ToastUtil;

import org.kymjs.kjframe.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityAddActivity extends BaseAppActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.et_community_content)
    EditText mCommunityContextEditText;

    @BindView(R.id.all_note_image_list)
    RecyclerView mAllNoteImagesRecyclerView;

    @BindView(R.id.btn_send_note)
    Button mSendNoteButton;

    ImageSelectedAdapter mImageSelectedAdapter;

    List<String> mSelectedImages;

    private String noteType;

    OKHttpRequest okHttpRequest = null;

    private CustomProgress dialog;

    @Override
    public int getLayoutId() {
        return R.layout.community_note_add;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            noteType = bundle.getString("note_type");
        }

        okHttpRequest = new OKHttpRequest();

        mToolbar.setTitle("发帖");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSelectedImages = new ArrayList<String>();

        mSelectedImages.add(ImageUtil.ADD_PATH);

        mImageSelectedAdapter = new ImageSelectedAdapter(this, mSelectedImages);
        mAllNoteImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAllNoteImagesRecyclerView.setAdapter(mImageSelectedAdapter);

        if (dialog == null) {
            dialog = CustomProgress.create(context, "正在发送...", true, null);
        }

        mSendNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mCommunityContextEditText.getText())) {

                    ToastUtil.toast(CommunityAddActivity.this, "请输入发帖内容");
                    return;
                }

                if (mImageSelectedAdapter.getData() != null) {
                    if (mImageSelectedAdapter.getData().size() == 1 && mImageSelectedAdapter.getData().get(0).equals(ImageUtil.ADD_PATH)) {
                        ToastUtil.toast(CommunityAddActivity.this, "请选择图片");
                        return;
                    }

                    dialog.show();

                    List<File> files = new ArrayList<File>();
                    try {
                        for (int i = 0; i < mImageSelectedAdapter.getData().size(); i++) {
                            if (!mImageSelectedAdapter.getData().get(i).equals(ImageUtil.ADD_PATH)) {
                                File tempFile = new File(mImageSelectedAdapter.getData().get(i));
                                files.add(tempFile);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", App.loginUser != null ? App.loginUser.id + "" : "");
                    params.put("content", mCommunityContextEditText.getText().toString());
                    params.put("type", noteType != null ? noteType : "");

                    okHttpRequest.aget(Server.ADD_NOTE_URL, params, files, new OnResponseListener() {
                        @Override
                        public void onSuccess(String response) {
                            if(dialog != null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                CommunityInfo result = Contants.gson.fromJson(response, CommunityInfo.class);
                                if (result != null) {
                                    ToastUtil.toast(CommunityAddActivity.this, "发帖成功");

                                    RxBus.get().post(Contants.COMMUNITY_REFRESH, "form add");
                                    RxBus.get().post(Contants.COMMUNITY_ADD_REFRESH, noteType);

                                    finish();
                                } else {
                                    ToastUtil.toast(CommunityAddActivity.this, "发帖失败,请稍后重试");
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if(dialog != null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onBefore() {

                        }
                    });


                } else {
                    if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    ToastUtil.toast(CommunityAddActivity.this, "发帖失败");
                }
            }
        });

        mImageSelectedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    selectImages();
                }
            }
        });

        mImageSelectedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                mImageSelectedAdapter.getData().remove(position);

                if (mImageSelectedAdapter.getData() != null && mImageSelectedAdapter.getData().size() < 3) {

                    if (!mImageSelectedAdapter.getData().contains(ImageUtil.ADD_PATH)) {
                        mImageSelectedAdapter.getData().add(0, ImageUtil.ADD_PATH);
                    }
                }
                mImageSelectedAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public void selectImages() {
        PhotoPicker.builder()
                .setPhotoCount(3)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(CommunityAddActivity.this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mSelectedImages = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (mSelectedImages != null && mSelectedImages.size() < 3) {
                    mSelectedImages.add(0, ImageUtil.ADD_PATH);
                }
                mImageSelectedAdapter.setNewData(mSelectedImages);
            }
        }
    }
}
