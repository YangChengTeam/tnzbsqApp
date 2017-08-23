package com.fy.tnzbsq.common;

import com.fy.tnzbsq.bean.GameInfo;

/**
 * Created by zhangkai on 16/4/10.
 */
public interface CustomWebViewDelegate extends BaseCustomeWebViewDelegate {
    void setTitle(String title);
    void hide();
    void show();
    void startFullActivity(GameInfo gameInfo);
    void setUrl(String url);
    void showWheelView();
    void createImage(String id, String data,String isVip,String price);
    void saveImage(String imageRealPath);
    void addKeep(String id);
    void imageShow(String path);
    void updateVersion();
    void selectPic(int xvalue, int yvalue);
    void photoGraph();
    void submitMesage(String str, String description);
    void clearCache();
    void toSave();
    void toShare();
    void initWithUrl(String url);
    void updateUserName(String userName);
    void setShowState(boolean flag);
    void loadImageList(int currentPage);
    void search(String keyword);
    void fightMenu();
}
