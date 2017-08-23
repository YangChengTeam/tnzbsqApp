package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.HotWordsAdapter;
import com.fy.tnzbsq.bean.HotWordListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.util.ImageUtil;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.ShareActsDialog;
import com.fy.tnzbsq.view.ShareFightDialog;
import com.fy.tnzbsq.view.image.GLFont;
import com.fy.tnzbsq.view.image.StickerView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class ImageMakeActivity extends BaseActivity {

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.share_layout, click = true)
    private LinearLayout shareLayout;

    @BindView(id = R.id.image_make_hot_input)
    private EditText imageMakeEt;

    @BindView(id = R.id.fight_gif, click = true)
    private GifImageView fightGifView;

    @BindView(id = R.id.sticker_view, click = true)
    private StickerView stickerView;

    @BindView(id = R.id.image_view_layout)
    private FrameLayout imageViewLayout;

    @BindView(id = R.id.hot_layout)
    private LinearLayout hotLayout;

    @BindView(id = R.id.hot_words_list)
    private ListView hotWordListView;

    // 底图
    private Bitmap mainBitmap;
    // 绘制的文字标签图
    private Bitmap textBitmap;

    private Bitmap showBitmap;

    private List<String> hotData;

    private Typeface typeFace;

    private Bitmap tempBitmap;

    private String imagePath = "";

    private CustomProgress dialog;

    private String id;

    private String color = "000000";

    private File tempFile;

    private String tempType = "jpg";

    private String tempFightName;

    private File resultFile;

    private GifDecoder decoder;

    private String position = "";

    //绘制图片区域新的高度
    private int newHeight = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ShareActsDialog shareActsDialog = new ShareActsDialog(context, showBitmap, tempFightName, "gif");
                    shareActsDialog.showShareDialog(shareActsDialog);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void setRootView() {
        setContentView(R.layout.image_make);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        super.initData();

        hotData = new ArrayList<String>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // 获取图片的下载路径
        if (bundle != null && bundle.getString("imagePath") != null && bundle.getString("imagePath").length() > 0) {
            imagePath = intent.getExtras().getString("imagePath");
        }

        // ID
        if (bundle != null && bundle.getString("id") != null && bundle.getString("id").length() > 0) {
            id = intent.getExtras().getString("id");
        }

        // color
        if (bundle != null && bundle.getString("color") != null && bundle.getString("color").length() > 0) {
            color = intent.getExtras().getString("color");
        }

        // color
        if (bundle != null && bundle.getString("position") != null && bundle.getString("position").length() > 0) {
            position = intent.getExtras().getString("position");
        }

        titleNameTv.setText(getResources().getString(R.string.image_make_text));

        //mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);

        mainBitmap = ImageUtil.transparentBitmap(DensityUtils.getScreenW(context), DensityUtils.dip2px(context, 225) - 70, 0);

        typeFace = Typeface.create("宋体", Typeface.BOLD);
        textBitmap = GLFont.getImage(600, 100, "", DensityUtils.sp2px(context, 25), DensityUtils.getScreenW(context), typeFace, "#" + color);
        //初始化绘制图片
        //stickerView.setWaterMark(textBitmap, mainBitmap, stickerView.getHeight(),position,"腾牛装逼神器");

        //new ImageLoaderAsyncTask().execute();

        hotWordListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageMakeEt.setText(hotData.get(position));
            }
        });

        imageMakeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (stickerView != null && mainBitmap != null) {

                    Log.e("TextChangedListener---", "TextChangedListener---");

                    stickerView.clearBitmap();
                    textBitmap = GLFont.getImage(600, 100, s.toString(), DensityUtils.sp2px(context, 25), DensityUtils.getScreenW(context), typeFace,
                            "#" + color);
                    // 合并图片
                    stickerView.setWaterMark(textBitmap, mainBitmap, stickerView.getHeight(), position, "腾牛装逼神器");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        stickerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (tempType.toLowerCase().equals("gif")) {
                    if (mainBitmap != null && textBitmap != null) {
                        if (dialog == null) {
                            dialog = CustomProgress.create(context, "动态图合成中...", true, null);
                        }

                        dialog.setCanceledOnTouchOutside(false);
                        if (isValidContext(context) && dialog != null) {
                            dialog.show();
                        }

                        new GifCreateAsyncTask().execute();
                    } else {
                        Toast.makeText(context, "无法加载图片，请稍后分享", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mainBitmap != null && textBitmap != null) {
                        showBitmap = stickerView.saveFightBitmapToFile(newHeight);
                        new QuietImageAsyncTask().execute();
                    } else {
                        Toast.makeText(context, "无法加载图片，请稍后分享", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });


        KJBitmap kjb = new KJBitmap();
        tempType = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length());
        tempFightName = Contants.BASE_IMAGE_DIR + File.separator + "temp_fight." + tempType;
        tempFile = new File(tempFightName);
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        kjb.saveImage(context, imagePath, tempFightName, true, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                if (dialog == null) {
                    dialog = CustomProgress.create(context, "图片加载中...", true, null);
                }
                dialog.setCanceledOnTouchOutside(false);
                if (isValidContext(context) && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(byte[] t) {
                super.onSuccess(t);
                //获取热门词汇列表
                new HotWordListAsyncTask().execute();
                try {

                    // 获取尺寸信息
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inJustDecodeBounds = true;
                    if (tempType.toLowerCase().equals("gif")) {

                        Bitmap tempBmp = BitmapFactory.decodeFile(tempFightName, op);

                        double tempPro = (double) op.outHeight / (double) op.outWidth;
                        double tempHeight = tempPro * DensityUtils.getScreenW(context);

                        fightGifView.setLayoutParams(new FrameLayout.LayoutParams(DensityUtils.getScreenW(context), (int) tempHeight));
                        fightGifView.setImageDrawable(new GifDrawable(tempFile.getAbsolutePath()));

                        newHeight = (int) tempHeight;

                        mainBitmap = ImageUtil.transparentBitmap(DensityUtils.getScreenW(context), (int) tempHeight, 0);
                    } else {
                        fightGifView.setVisibility(View.GONE);

                        mainBitmap = BitmapFactory.decodeFile(tempFightName);

                        if (mainBitmap == null) {
                            mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
                        }

                        double tempPro = (double) mainBitmap.getHeight() / (double) mainBitmap.getWidth();
                        newHeight = (int) (DensityUtils.getScreenW(context) * tempPro);

                        //Log.e("tempPro","宽高比---"+tempPro);
                        //Log.e("tempPro","新高度---"+newHeight);

                        //缩放图片
                        mainBitmap = ZoomImg(mainBitmap, DensityUtils.getScreenW(context), newHeight);
                    }

                    LinearLayout.LayoutParams imageLayout = (LinearLayout.LayoutParams) imageViewLayout.getLayoutParams();
                    imageLayout.height = newHeight;
                    imageViewLayout.setLayoutParams(imageLayout);

                } catch (IOException e) {
                    e.printStackTrace();
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

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(ImageMakeActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(intent);
    }*/

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.back_img:
                finish();
               /* Intent intent = new Intent(ImageMakeActivity.this,MainActivity.class);
                startActivity(intent);*/
                break;
            case R.id.share_layout:
                if (tempType.toLowerCase().equals("gif")) {
                    if (mainBitmap != null && textBitmap != null) {
                        if (dialog == null) {
                            dialog = CustomProgress.create(context, "动态图合成中...", true, null);
                        }

                        dialog.setCanceledOnTouchOutside(false);
                        if (isValidContext(context) && dialog != null) {
                            dialog.show();
                        }

                        new GifCreateAsyncTask().execute();
                    } else {
                        Toast.makeText(context, "无法加载图片，请稍后分享", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mainBitmap != null && textBitmap != null) {
                        showBitmap = stickerView.saveFightBitmapToFile(newHeight);
                        new QuietImageAsyncTask().execute();
                    } else {
                        Toast.makeText(context, "无法加载图片，请稍后分享", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            default:
                break;
        }
    }

    long end;

    /**
     * 异步加载网络图片并保存到本地
     */
    public class GifCreateAsyncTask extends AsyncTask<Integer, Integer, String> {

        List<Bitmap> resultBitmaps;

        int gifWidth = 0;
        int gifHeight = 0;

        long start = (new Date()).getTime();

        public GifCreateAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            List<Bitmap> gifBitmaps = new ArrayList<>();
            try {
                long start = new Date().getTime();

                MockProvider provider = new MockProvider();
                GifHeaderParser headerParser = new GifHeaderParser();
                InputStream in = new FileInputStream(tempFile);
                byte[] data = FileUtils.input2byte(in);
                headerParser.setData(data);
                GifHeader header = headerParser.parseHeader();
                decoder = new GifDecoder(provider);
                decoder.setData(header, data);


                for (int i = 0; i < decoder.getFrameCount(); i++) {
                    decoder.advance();
                    Bitmap tempBitmap = decoder.getNextFrame();
                    gifBitmaps.add(tempBitmap);
                }

                resultBitmaps = stickerView.getGifBitmaps(gifBitmaps);

                end = new Date().getTime();
                com.umeng.socialize.utils.Log.e("create-time", "create-time===" + (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private class MockProvider implements GifDecoder.BitmapProvider {

            @Override
            public Bitmap obtain(int width, int height, Bitmap.Config config) {
                Bitmap result = Bitmap.createBitmap(width, height, config);
                return result;
            }

            @Override
            public void release(Bitmap bitmap) {
                // Do nothing.
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            String result = "success";
            resultFile = new File(Contants.BASE_SD_DIR + File.separator + "temp_fight_result.gif");

            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.start(resultFile.getAbsolutePath());
            e.setRepeat(0);
            e.setQuality(80);
            for (int j = 0; j < resultBitmaps.size(); j++) {
                // Bitmap is MUST ARGB_8888.
                e.addFrame(resultBitmaps.get(j));
                e.setDelay(decoder.getDelay(j));
            }

            e.finish();
            long end1 = new Date().getTime();
            Log.e("create-time", "create-time===" + (end1 - end));
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (result != null && result.equals("success") && resultFile.exists()) {
                ShareFightDialog shareFightDialog = new ShareFightDialog(context, resultFile.getAbsolutePath());
                shareFightDialog.showShareDialog(shareFightDialog);
            } else {
                Toast.makeText(context, "图片合成失败，请稍后分享", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("你这个逗逼");
        data.add("别说话，小心我打死你");
        data.add("这个逼装的我给满分!");
        data.add("你这么吊，你父母知道吗");
        data.add("你再这么叽叽歪歪，我打死你!");
        return data;
    }

    /**
     * 异步加载网络图片并保存到本地
     */
    public class ImageLoaderAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {
        public ImageLoaderAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            if (dialog == null) {
                dialog = CustomProgress.create(context, "数据加载中...", true, null);
            }
            dialog.setCanceledOnTouchOutside(false);
            if (isValidContext(context) && dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            try {
                byte[] data = readInputStream(getRequest(imagePath));
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

            if (result != null) {
                mainBitmap = result;
            }

            int DISPLAY_HEIGHT = DensityUtils.getScreenH(context) - DensityUtils.dip2px(context, 225) - 70;

            if (mainBitmap == null) {
                mainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.frame_word_default);
            }

            double tempPro = (double) mainBitmap.getHeight() / (double) mainBitmap.getWidth();
            double newWidth = ((double) DISPLAY_HEIGHT) / tempPro;

            //mainBitmap = ZoomImg(mainBitmap, (int) newWidth, DISPLAY_HEIGHT);

            mainBitmap = ImageUtil.transparentBitmap((int) newWidth, DISPLAY_HEIGHT, 0);

            //mainBitmap = ZoomImg1(mainBitmap, (int) newWidth, DISPLAY_HEIGHT);

            // 绘制图片
            stickerView.setWaterMark(textBitmap, mainBitmap, stickerView.getHeight(), position, "腾牛装逼神器");
            result = null;
            new HotWordListAsyncTask().execute();
        }
    }

    /**
     * 异步加载热门配文
     */
    public class HotWordListAsyncTask extends AsyncTask<Integer, Integer, HotWordListRet> {
        public HotWordListAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HotWordListRet doInBackground(Integer... params) {
            return ServiceInterface.getHotWordListById(context, id);
        }

        @Override
        protected void onPostExecute(HotWordListRet result) {
            super.onPostExecute(result);

            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (result != null) {
                hotData.clear();
                if (result.data != null && result.data.size() > 0) {
                    hotData = result.data;
                } else {
                    hotData.addAll(getData());
                }
                // 绘制图片
                //stickerView.setWaterMark(textBitmap, mainBitmap, newHeight,position,"腾牛装逼神器");

            } else {
                hotData.clear();
                hotData.addAll(getData());
            }
            // 热门配文
            hotWordListView.setAdapter(new HotWordsAdapter(context, hotData));
            imageMakeEt.setText(hotData.get(0));
        }
    }



    public class QuietImageAsyncTask extends AsyncTask<Integer, Integer, String> {
        public QuietImageAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            if (dialog == null) {
                dialog = CustomProgress.create(context, "图片合成中...", true, null);
            }
            dialog.setCanceledOnTouchOutside(false);
            if (isValidContext(context) && dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            String result = "success";
            try {
                if (showBitmap == null) {
                    showBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
                }
                //showBitmap = ImageUtil.compressImage(showBitmap, 32);//将图片压缩到32K以内，才能发送微信表情
                try {
                    FileUtils.bitmapToFile(showBitmap, tempFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "fail";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (result != null && result.equals("success")) {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            } else {
                Toast.makeText(context, "图片合成失败，请稍后分享", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static InputStream getRequest(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // 5秒
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

    protected void onActivityResult(int resultCode, int arg1, Intent arg2) {
        super.onActivityResult(resultCode, arg1, arg2);
        try {
            UMShareAPI.get(this).onActivityResult(resultCode, arg1, arg2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理图片
     *
     * @param bm    所要转换的bitmap
     * @param '新的宽'
     * @param '新的高'
     * @return 指定宽高的bitmap
     */
    public Bitmap ZoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public Bitmap ZoomImg1(Bitmap bm, int newWidth, int newHeight) {
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, newWidth, newHeight);
        return newbm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        position = "";
        if (mainBitmap != null) {
            mainBitmap.recycle();
            mainBitmap = null;
        }
        if (textBitmap != null) {
            textBitmap.recycle();
            textBitmap = null;
        }
        if (showBitmap != null) {
            showBitmap.recycle();
            showBitmap = null;
        }
        if (tempBitmap != null) {
            tempBitmap.recycle();
            tempBitmap = null;
        }
    }
}
