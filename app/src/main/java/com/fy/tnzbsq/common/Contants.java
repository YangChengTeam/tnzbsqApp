package com.fy.tnzbsq.common;

import android.os.Environment;

import com.fy.tnzbsq.App;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class Contants {
	public final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
			.setPrettyPrinting().create();


	public static String SD_DIR = App.sdPath;
	public static final String BASE_SD_DIR = SD_DIR + File.separator + "TNZBSQ";
	public static final String BASE_NORMAL_FILE_DIR = BASE_SD_DIR + File.separator + "files";
	public static final String BASE_IMAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "photo";// 用隐藏文件，不让看见；
	public static final String BASE_FIGHT_IMAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fight_photo";// 用隐藏文件，不让看见；
	public static final String BASE_HEAD_IMAGE_DIR = BASE_SD_DIR + File.separator + "headimage";

	public static final String GAME_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "feiyousdk";// 用隐藏文件，不让看见；

	public static final String USER_INFO = "userInfo";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String PASS_WORD = "passWord";

	// 保存到SharePreferences中的登录的相关数据的文件
	public static final String START_DATA = "start_data";
	// 是否首次登录
	public static final String IS_FIRST = "is_first";

	public static final String MZ_DATA = "mz_data";
	public static final String IS_AGREE = "is_agree";
	
	public static final String UPDATE_TIME = "update_time";
	public static final String CURRENT_DATA = "current_data";
	
	public static final String FIRST_SHARE_DATA = "first_share_data";
	public static final String IS_FIRST_SHARE = "is_first_share";

	public static final String FIRST_CUT = "first_cut";
	public static final String IS_FIRST_CUT = "is_first_cut";

	public static final String FIRST_CREATE = "first_create";
	public static final String IS_FIRST_CREATE = "is_first_create";

	public static final String NEW_DATA_SHOW = "new_data_show";
	public static final String NEW_DATA_RESULT = "new_data_result";

	public static final String USER_INFO_NAME = "user_info_name";
	
	public static final String COMMON_URL = "url";
	
	public static final int NOTIFICATION_ID_UPDATE = 0x0;

	public static final int NOTIFICATION_LSDD_ID = 0x1;

	//APP所有数据文件的存储路径
	public static final String ALL_DATA_DIR_PATH = BASE_SD_DIR +  File.separator + "data";
	//APP装逼所有数据文件名
	public static final String ALL_DATA_FILENAME = "data.js";
	//取到的比最后一个ID大的数据集合文件
	public static final String NEW_DATA_FILENAME = "new_data.js";
	//APP斗图所有数据文件名
	public static final String ALL_FIGHT_DATA_FILENAME = "data_dt.js";
	//搜索所有数据的文件名
	public static final String SEARCH_DATA_FILENAME = "search_data.js";
	//我的收藏数据文件名
	public static final String KEEP_DATA_FILENAME = "my_keep_data.js";
	
	public static final String VERSION_CHECK = "version_check";
	public static final String VERSION_CHECK_DATA = "version_check_data";

	public static final String VERSION_CODE = "version_code";
	public static final String VERSION_CODE_DATA = "version_code_data";

	public static final String ALL_SMALL_IMAGE_PATH = BASE_SD_DIR +  File.separator + "SMALL_IMAGE";

	//----------个性头像下载地址-----------------
	public static String HEAD_STYLE_DOWN_URL = "http://nz.qqtn.com/zbsq/Apk/gxtx_qqtn.apk";

	public static String HEAD_STYLE_SOURCE_FILE_PATH = BASE_NORMAL_FILE_DIR +File.separator +"headstyle.apk";

	public static String HEAD_STYLE_DOWN_FILE_PATH = BASE_NORMAL_FILE_DIR +File.separator +"headstyle_down.apk";
	//-------------------------------------------

	//----------铃声多多下载地址------------------
	public static String LSDD_DOWN_URL = "http://vip.cr173.com/Apk/sound_zbsq.apk";

	public static String LSDD_SOURCE_FILE_PATH = BASE_NORMAL_FILE_DIR +File.separator +"lsdd_from_zbsq.apk";

	public static String LSDD_DOWN_FILE_PATH = BASE_NORMAL_FILE_DIR +File.separator +"lsdd_from_zbsq_down.apk";
	//-------------------------------------------

	public static String GAME_INSTALL_URL = "http://apk.6071.com/diguoshidai/diguoshidai_zbsq.apk";

	public static String GAME_DOWN_FILE_PATH = GAME_DIR +File.separator +"game_down.apk";

	public static final String GAME_PACKAGE_NAME = "com.regin.dgsd.leqi6071";

    public static final String COMMUNITY_ADD = "community_add";

}
