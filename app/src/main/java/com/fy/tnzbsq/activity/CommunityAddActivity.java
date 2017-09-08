package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ImageSelectedAdapter;
import com.fy.tnzbsq.bean.CommunityInfo;
import com.fy.tnzbsq.bean.UpFileInfo;
import com.fy.tnzbsq.util.ImageUtil;
import com.kk.utils.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.kymjs.kjframe.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityAddActivity extends BaseAppActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.et_community_content)
    EditText mCommunityContextEditText;

    @BindView(R.id.all_note_image_list)
    RecyclerView mAllNoteImagesRecyclerView;

    @BindView(R.id.btn_send_note)
    Button mSendNoteButton;

    ImageSelectedAdapter mImageSelectedAdapter;

    List<Uri> mSelectedImages;

    private Uri mAddUri;

    private String noteType;

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

        mAddUri = ImageUtil.getAddUri(CommunityAddActivity.this);

        mSelectedImages = new ArrayList<Uri>();

        mSelectedImages.add(ImageUtil.getAddUri(this));

        mImageSelectedAdapter = new ImageSelectedAdapter(this, mSelectedImages);
        mAllNoteImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAllNoteImagesRecyclerView.setAdapter(mImageSelectedAdapter);

        mSendNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mCommunityContextEditText.getText())) {

                    ToastUtil.toast(CommunityAddActivity.this, "请输入发帖内容");
                    return;
                }

                if (mImageSelectedAdapter.getData() != null) {
                    if (mImageSelectedAdapter.getData().size() == 1 && mImageSelectedAdapter.getData().get(0).compareTo(mAddUri) == 0) {
                        ToastUtil.toast(CommunityAddActivity.this, "请选择图片");
                        return;
                    }

                    UpFileInfo upFileInfo = new UpFileInfo();
                    try {
                        upFileInfo.filename = "file.jpg";
                        upFileInfo.name = "file";
                        List<File> files = new ArrayList<File>();
                        for (int i = 0; i < mImageSelectedAdapter.getData().size(); i++) {
                            if (mImageSelectedAdapter.getData().get(i).compareTo(mAddUri) != 0) {
                                File tempFile = new File(ImageUtil.getPathBuUri(CommunityAddActivity.this, mImageSelectedAdapter.getData().get(i)));
                                files.add(tempFile);
                            }
                        }
                        upFileInfo.files = files;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CommunityInfo tempCommunityInfo = new CommunityInfo();
                    tempCommunityInfo.setUser_id("3");
                    tempCommunityInfo.setContent(mCommunityContextEditText.getText().toString());
                    tempCommunityInfo.setType(noteType != null ? noteType : "");

                    //mPresenter.addCommunityInfo(tempCommunityInfo, upFileInfo);

                } else {
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

                    if (!mImageSelectedAdapter.getData().contains(mAddUri)) {
                        mImageSelectedAdapter.getData().add(0, mAddUri);
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
        Set<MimeType> sets = new HashSet<MimeType>();
        sets.add(MimeType.PNG);
        sets.add(MimeType.JPEG);

        Matisse.from(CommunityAddActivity.this)
                .choose(sets)
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .maxSelectable(3)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            /*mSelectedImages = Matisse.obtainResult(data);

            if (mSelectedImages != null && mSelectedImages.size() < 3) {
                mSelectedImages.add(0, DrawableUtils.getAddUri(this));
            }
            mImageSelectedAdapter.setNewData(mSelectedImages);*/
        }
    }
}
