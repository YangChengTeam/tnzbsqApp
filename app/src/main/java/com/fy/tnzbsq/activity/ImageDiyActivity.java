package com.fy.tnzbsq.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.BubbleAdapter;
import com.fy.tnzbsq.adapter.TypeFaceAdapter;
import com.fy.tnzbsq.bean.WordCreateRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.ImageUtil;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.HorizontialListView;
import com.fy.tnzbsq.view.ShareFightDialog;
import com.fy.tnzbsq.view.image.GLFont;
import com.fy.tnzbsq.view.image.StickerView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.PreferenceHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ImageDiyActivity extends BaseActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    public static int DOUBLE_CLICK = 2;

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.share_layout, click = true)
    private LinearLayout shareLayout;

    @BindView(id = R.id.pic_layout, click = true)
    private LinearLayout picLayout;

    @BindView(id = R.id.photo_layout, click = true)
    private LinearLayout photoLayout;

    @BindView(id = R.id.gallery_layout, click = true)
    private LinearLayout galleryLayout;

    @BindView(id = R.id.image_make_diy_input)
    private EditText imageMakeEt;

    @BindView(id = R.id.create_word_btn, click = true)
    private Button wordCreateBtn;

    @BindView(id = R.id.sticker_diy_view, click = true)
    private StickerView stickerView;

    @BindView(id = R.id.type_face_list)
    private HorizontialListView typefaceListView;

    @BindView(id = R.id.create_color_list)
    private HorizontialListView createColorListView;

    @BindView(id = R.id.bubble_list)
    private HorizontialListView bubbleListView;

    private TypeFaceAdapter adapter;

    private TypeFaceAdapter colorAdapter;

    private BubbleAdapter bubbleAdapter;

    // 底图
    private Bitmap mainBitmap;
    // 绘制的文字标签图
    private Bitmap textBitmap;
    // 气泡贴图标签
    private Bitmap bubbleBitmap;

    private Bitmap showBitmap;

    private Bitmap tempBitmap;

    private static final int[] typedfaceData = {R.mipmap.typeface_default, R.mipmap.typeface_icon1,
            R.mipmap.typeface_icon2, R.mipmap.typeface_icon3, R.mipmap.typeface_icon4, R.mipmap.typeface_icon5,
            R.mipmap.typeface_icon6, R.mipmap.typeface_icon7, R.mipmap.typeface_icon8, R.mipmap.typeface_icon9,
            R.mipmap.typeface_icon10, R.mipmap.typeface_icon11, R.mipmap.typeface_icon12,
            R.mipmap.typeface_icon13, R.mipmap.typeface_icon14};

    private static final int[] colorData = {R.mipmap.color_default, R.mipmap.create_color1,
            R.mipmap.create_color2, R.mipmap.create_color3, R.mipmap.create_color4, R.mipmap.create_color5,
            R.mipmap.create_color6, R.mipmap.create_color7, R.mipmap.create_color8};

    private static final int[] bubbleData = {R.mipmap.bubble_default, R.mipmap.bubble1, R.mipmap.bubble2,
            R.mipmap.bubble3, R.mipmap.bubble4, R.mipmap.bubble5, R.mipmap.bubble6, R.mipmap.bubble7,
            R.mipmap.bubble8, R.mipmap.bubble9, R.mipmap.bubble10};

    private static final String[] colorDataRGB = {"#000000", "#ff0000", "#ffff00", "#00ff00", "#00ffff", "#00a2e8",
            "#ff00ff", "#ffaec9", "#cccc00"};

    // 使用的字体的编号
    private int typefaceNum;

    private Typeface typeFace;

    // 使用的颜色的编号
    private int colorNum;

    private CustomProgress dialog;

    private String wordImagePath;

    private Dialog tipDialog;

    private GestureDetectorCompat mDetector;

    @Override
    public void setRootView() {
        setContentView(R.layout.image_diy);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    private String imagePath = "";

    private String path = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Main5ActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        ImageDiyActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showRecord() {
        //ToastUtils.showLong("允许使用相机");

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HeadImageUtils.cutPhoto = null;
            HeadImageUtils.getFromCamara(ImageDiyActivity.this);
        } else {
            Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onRecordDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForRecord(PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onStorageNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initData() {
        super.initData();

        HeadImageUtils.cutPhoto = null;

        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);

        titleNameTv.setText(getResources().getString(R.string.image_make_text));
        adapter = new TypeFaceAdapter(context, typedfaceData);
        colorAdapter = new TypeFaceAdapter(context, colorData);
        bubbleAdapter = new BubbleAdapter(context, bubbleData);

        typefaceListView.setAdapter(adapter);
        createColorListView.setAdapter(colorAdapter);
        bubbleListView.setAdapter(bubbleAdapter);

        typeFace = Typeface.create("宋体", Typeface.BOLD);

        // 字体改变时，重新绘制图片
        typefaceListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mainBitmap != null && imageMakeEt.getText() != null
                        && imageMakeEt.getText().toString().length() > 0) {

                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetChanged();
                    typefaceNum = position;

                    stickerView.clearBitmap();

                    if (typefaceNum == 0) {
                        textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80, DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                        if (bubbleBitmap != null) {
                            // 合并气泡 与文字
                            stickerView.setWaterMarkImageBullbe(bubbleBitmap, textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        } else {
                            // 合并图片
                            stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        }
                    } else {
                        String temp = "";
                        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
                        Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
                        if (m.find()) {
                            temp = m.replaceAll("#!");
                        } else {
                            temp = imageMakeEt.getText().toString();
                        }

                        WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
                                "100", typefaceNum + "",
                                (DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 82)) + "");
                    }
                }
            }
        });

        // 颜色改变时，重新绘制图片
        createColorListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mainBitmap != null && imageMakeEt.getText() != null && imageMakeEt.getText().toString().length() > 0) {
                    colorAdapter.setSelectedPosition(position);
                    colorAdapter.notifyDataSetChanged();
                    colorNum = position;

                    stickerView.clearBitmap();

                    if (typefaceNum == 0) {
                        textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80, DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                        if (bubbleBitmap != null) {
                            // 合并气泡 与文字
                            stickerView.setWaterMarkImageBullbe(bubbleBitmap, textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        } else {
                            // 合并图片
                            stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        }
                    } else {
                        String temp = "";
                        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
                        Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
                        if (m.find()) {
                            temp = m.replaceAll("#!");
                        } else {
                            temp = imageMakeEt.getText().toString();
                        }

                        WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
                                "100", typefaceNum + "",
                                (DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 82)) + "");
                    }
                }
            }
        });

        // 气泡改变时，重新绘制图片
        bubbleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mainBitmap != null) {

                    bubbleAdapter.setSelectedPosition(position);
                    bubbleAdapter.notifyDataSetChanged();
                    bubbleBitmap = BitmapFactory.decodeResource(getResources(), bubbleData[position]);

                    stickerView.clearBitmap();

                    if (position == 0) {
                        bubbleBitmap = null;
                    }

                    stickerView.clearBitmap();

                    if (typefaceNum == 0) {

                        if(StringUtils.isEmpty(imageMakeEt.getText())){
                            textBitmap = null;
                        }else{
                            textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80,
                                    DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                        }
                        if (position > 0) {
                            // 合并气泡 与文字
                            stickerView.setWaterMarkImageBullbe(bubbleBitmap, textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        }
                    } else {
                        if (imageMakeEt.getText() != null
                                && imageMakeEt.getText().toString().length() > 0) {

                            String temp = "";
                            Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
                            Matcher m = CRLF.matcher(imageMakeEt.getText().toString());
                            if (m.find()) {
                                temp = m.replaceAll("#!");
                            } else {
                                temp = imageMakeEt.getText().toString();
                            }

                            WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()), temp,
                                    "100", typefaceNum + "",
                                    (DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 82)) + "");
                        }
                    }
                }
            }
        });


        //长按绘图区域，弹出分享保存对话窗口
        stickerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mainBitmap != null && stickerView.getVisibility() == View.VISIBLE) {
                    showBitmap = stickerView.saveBubbleBitmapToFile();
                    if (showBitmap == null) {
                        showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
                    }
                    ShareFightDialog shareFightDialog = new ShareFightDialog(context, showBitmap);
                    shareFightDialog.showShareDialog(shareFightDialog);
                }
                return true;
            }
        });

        stickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return onTouchEvent(event);
            }
        });

        //首次加载制图页面时，提示用户如何操作
        tipDialog = new Dialog(this, R.style.Dialog_Fullscreen);
        tipDialog.setContentView(R.layout.create_dialog_tip);
        TextView iknowTv = (TextView) tipDialog.findViewById(R.id.confirm_img);
        iknowTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        iknowTv.getPaint().setAntiAlias(true);//抗锯齿

        iknowTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidContext(context) && tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
            }
        });

        CustomProgress shareDialog = CustomProgress.create(context, "正在分享...", true, null);
        shareDialog.setTitle("装B神器分享");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.share_layout:
                if (mainBitmap != null) {
                    showBitmap = stickerView.saveBubbleBitmapToFile();
                    if (showBitmap == null) {
                        showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
                    }
                    ShareFightDialog shareFightDialog = new ShareFightDialog(context, showBitmap);
                    shareFightDialog.showShareDialog(shareFightDialog);
                } else {
                    Toast.makeText(context, "未选择图片，请制作后稍后分享", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.photo_layout:
                ImageDiyActivityPermissionsDispatcher.showRecordWithCheck(this);

                break;
            case R.id.gallery_layout:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    HeadImageUtils.cutPhoto = null;
                    HeadImageUtils.getFromLocation(ImageDiyActivity.this);
                } else {
                    Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create_word_btn:
                if (typefaceNum == 0) {
                    stickerView.clearBitmap();

                    // 输入框有文字的时候才绘制文字图片
                    if (imageMakeEt.getText().toString().length() > 0) {
                        textBitmap = GLFont.getImage(600, 100, imageMakeEt.getText().toString(), 80, DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                        if (bubbleBitmap != null) {
                            // 合并气泡 与文字
                            stickerView.setWaterMarkImageBullbe(bubbleBitmap, textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        } else {
                            // 合并图片
                            stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                        }
                    }
                } else {
                    WordCreateData(colorDataRGB[colorNum].substring(1, colorDataRGB[colorNum].length()),
                            imageMakeEt.getText().toString(), "100", typefaceNum + "",
                            (DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 82)) + "");
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (requestCode) {
            case HeadImageUtils.FROM_CRAMA:
            /*if (HeadImageUtils.photoCamare != null) {
                HeadImageUtils.cutCorePhoto(ImageDiyActivity.this, HeadImageUtils.photoCamare);
			}*/

                if (resultCode == RESULT_OK && HeadImageUtils.photoCamare != null) {
                    //----获取图片的真实路径
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    if (filePathColumn != null && filePathColumn.length > 0) {
                        Cursor cursor = getContentResolver().query(HeadImageUtils.photoCamare, filePathColumn, null, null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePath = cursor.getString(columnIndex);
                            HeadImageUtils.imgPath = imagePath;
                        }
                    }

                    Intent intent1 = new Intent(ImageDiyActivity.this, ImageCropActivity.class);
                    startActivityForResult(intent1, HeadImageUtils.FREE_CUT);
                }


                break;
            case HeadImageUtils.FROM_LOCAL:
            /*if (data != null && data.getData() != null) {
                HeadImageUtils.cutCorePhoto(ImageDiyActivity.this, data.getData());
			}*/

                /*if (resultCode == RESULT_OK && data != null) {

                    Uri selectedImage = data.getData();
                    //----获取图片的真实路径
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    if (filePathColumn != null && filePathColumn.length > 0) {
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePath = cursor.getString(columnIndex);
                            HeadImageUtils.imgPath = imagePath;
                        }
                    }
                }*/

                if (data != null) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(uri,
                                new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        if (null == cursor) {
                            Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cursor.moveToFirst();
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    } else {
                        path = uri.getPath();
                    }
                } else {
                    Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                    stickerView.clearBitmap();
                    //绘制图片
                    stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                    return;
                }

                Log.e("path----", "image-path---" + path);

                if (path != null && path.length() > 0) {
                    Log.e("start-crop----", "start-crop---");

                    stickerView.clearBitmap();

                    HeadImageUtils.imgPath = path;
                    //path = "";

                    Intent intent = new Intent(ImageDiyActivity.this, ImageCropActivity.class);
                    startActivityForResult(intent, HeadImageUtils.FREE_CUT);
                }

                break;
            case HeadImageUtils.FREE_CUT:
                boolean isCutSuccess = true;
                if (HeadImageUtils.cropBitmap != null) {
                    picLayout.setVisibility(View.GONE);
                    stickerView.setVisibility(View.VISIBLE);

                    mainBitmap = HeadImageUtils.cropBitmap;
                    changeBitmap();
                    textBitmap = GLFont.getImage(600, 100, "", 80, DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                    //绘制图片
                    stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), "");
                } else if (HeadImageUtils.imgResultPath != null) {
                    mainBitmap = BitmapFactory.decodeFile(HeadImageUtils.imgResultPath);
                    changeBitmap();
                    textBitmap = GLFont.getImage(600, 100, "", 80, DensityUtils.getScreenW(context), typeFace, colorDataRGB[colorNum]);
                    //绘制图片
                    stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), "");
                } else {
                    isCutSuccess = false;
                    Toast.makeText(context, "图片裁剪失败,请重试", Toast.LENGTH_SHORT).show();
                }

                //第一次裁剪成功时，提示用户
                if (isCutSuccess) {
                    boolean isFirst = PreferenceHelper.readBoolean(ImageDiyActivity.this, Contants.FIRST_CUT,
                            Contants.IS_FIRST_CUT, true);
                    if (isFirst) {
                        PreferenceHelper.write(ImageDiyActivity.this, Contants.FIRST_CUT, Contants.IS_FIRST_CUT, false);
                        tipDialog.show();
                    }
                }

                break;
            case HeadImageUtils.FROM_CUT:

                // 选择图像之后，修改用户的图像
                if (HeadImageUtils.cutPhoto != null) {
				/*
				 * if (HeadImageUtils.imgPath != null &&
				 * HeadImageUtils.imgPath.length() > 0) { updateUserInfo("", "",
				 * HeadImageUtils.imgPath); }
				 */

                    // updateUserInfo("", "", HeadImageUtils.imgPath);

                    picLayout.setVisibility(View.GONE);
                    stickerView.setVisibility(View.VISIBLE);

                    mainBitmap = decodeBitmap(HeadImageUtils.imgPath);

                    // mainBitmap =
                    // getBitmapByBytes(Bitmap2Bytes(BitmapFactory.decodeFile(HeadImageUtils.imgPath)));

                    textBitmap = GLFont.getImage(600, 100, "", 80, DensityUtils.getScreenW(context), typeFace,
                            colorDataRGB[colorNum]);

                    // 绘制图片
                    stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), "");
                }
                break;
        }

    }

    public int[] getData() {
        for (int i = 0; i < typedfaceData.length; i++) {
            typedfaceData[i] = R.mipmap.typeface_default;
        }
        return typedfaceData;
    }

    /**
     * 改变图片大小
     */
    public void changeBitmap() {
        int DISPLAY_HEIGHT = DensityUtils.getScreenH(context) - DensityUtils.dip2px(context, 285);

        // 图片的高度/宽度比
        double tempPro = (double) mainBitmap.getHeight() / (double) mainBitmap.getWidth();

        if (tempPro > 1) {
            double newWidth = ((double) DISPLAY_HEIGHT) / tempPro;
            mainBitmap = ImageUtil.ZoomImg(mainBitmap, (int) newWidth, DISPLAY_HEIGHT);
        } else {
            double newHeight = DensityUtils.getScreenW(context) * tempPro;
            mainBitmap = ImageUtil.ZoomImg(mainBitmap, DensityUtils.getScreenW(context), (int) newHeight);
        }

        if (mainBitmap == null) {
            mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
        }
    }

    /**
     * 从path中获取图片信息
     *
     * @param path
     * @return
     */
    private Bitmap decodeBitmap(String path) {

        float DISPLAY_WIDTH = DensityUtils.getScreenW(context);
        float DISPLAY_HEIGHT = DensityUtils.getScreenH(context) - DensityUtils.dip2px(context, 285);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
        // 获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
        int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_HEIGHT);
        // 如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }

        if (op.outHeight > DISPLAY_HEIGHT) {
            int tempWidth = (int) (DISPLAY_HEIGHT / (op.outHeight / op.outWidth));
            op.outWidth = tempWidth;
            op.outHeight = (int) DISPLAY_HEIGHT;
        }

        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);

        return bmp;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e("onDoubleTap---", "onDoubleTap---" + e.getAction());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HeadImageUtils.cutPhoto = null;
            HeadImageUtils.getFromLocation(ImageDiyActivity.this);
        } else {
            Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.e("onDoubleTapEvent", "onDoubleTapEvent--" + e.getAction());
        //ACTION_DOWN的值是 0,ACTION_UP的值是 1
        //判断为双击事件
        /*if(e.getAction() == DOUBLE_CLICK){
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                HeadImageUtils.cutPhoto = null;
                HeadImageUtils.getFromLocation(ImageDiyActivity.this);
            } else {
                Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }*/
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 异步加载网络图片并保存到本地
     */
    public class WordCreateAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {
        public WordCreateAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            try {
                byte[] data = readInputStream(getRequest(wordImagePath));
                tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                return tempBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tempBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result != null) {
                textBitmap = result;

                stickerView.clearBitmap();

                if (mainBitmap == null) {
                    mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
                }

                if (bubbleBitmap != null) {
                    // 合并气泡 与文字
                    stickerView.setWaterMarkImageBullbe(bubbleBitmap, textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                } else {
                    // 输入框有文字的时候才绘制文字图片
                    if (imageMakeEt.getText().toString().length() > 0) {
                        // 合并图片
                        stickerView.setWaterMarkImageDiy(textBitmap, mainBitmap, stickerView.getHeight(), imageMakeEt.getText().toString());
                    }
                }
                result = null;
            }
        }
    }

    /**
     * 文字处理接口
     *
     * @param color
     * @param text
     * @param size
     * @param font
     * @param wordimgwidth
     */
    private void WordCreateData(String color, String text, String size, String font, String wordimgwidth) {
        KJHttp kjh = new KJHttp();

        HttpParams params = new HttpParams();
        params.put("mime", App.ANDROID_ID);
        params.put("wcolor", color);
        params.put("wtext", text);
        params.put("wsize", size);
        params.put("wfont", font);
        params.put("wordimgwidth", wordimgwidth);
        kjh.post(Server.URL_WORD_CREATE_DATA, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

                if (dialog == null) {
                    dialog = CustomProgress.create(context, "", true, null);
                }
                dialog.setCanceledOnTouchOutside(false);
                if (isValidContext(context) && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(headers, t);
                // 获取cookie
                KJLoger.debug("===" + headers.get("Set-Cookie"));

                if (t != null && t.length > 0) {
                    String result = new String(t);
                    WordCreateRet ret = Contants.gson.fromJson(result, new TypeToken<WordCreateRet>() {
                    }.getType());

                    if (ret != null) {
                        // 当文字处理成功后，从网络加载文字图片
                        wordImagePath = ret.data;
                        new WordCreateAsyncTask().execute();
                    }
                } else {
                    Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
                    if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Toast.makeText(context, "在线文字处理失败,请稍后重试", Toast.LENGTH_SHORT).show();
                if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        });
    }

    public static InputStream getRequest(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000); // 5秒
        if (conn.getResponseCode() == 200) {
            return conn.getInputStream();
        }
        return null;

    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    @SuppressLint("NewApi")
    private boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainBitmap != null) {
            mainBitmap.recycle();
            mainBitmap = null;
        }
        if (textBitmap != null) {
            textBitmap.recycle();
            textBitmap = null;
        }
        if (bubbleBitmap != null) {
            bubbleBitmap.recycle();
            bubbleBitmap = null;
        }
        if (showBitmap != null) {
            showBitmap.recycle();
            showBitmap = null;
        }
        if (tempBitmap != null) {
            tempBitmap.recycle();
            tempBitmap = null;
        }

        if (HeadImageUtils.cropBitmap != null) {
            HeadImageUtils.cropBitmap.recycle();
            HeadImageUtils.cropBitmap = null;
        }
    }
}
