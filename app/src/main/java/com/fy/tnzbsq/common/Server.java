package com.fy.tnzbsq.common;

public class Server {
	public final static String URL_SERVER_IP = "http://nz.qqtn.com/zbsq/index.php?";
	public final static String URL_BASE_SERVER = "http://nz.qqtn.com/zbsq/index.php?";

	// 1、用户注册接口
	public final static String URL_REGISTER = URL_SERVER_IP + "m=Home&c=Index&a=mlogin";
	// 2、用户登录
	public final static String URL_LOGIN = URL_SERVER_IP + "m=Home&c=Index&a=mlogin";
	// 3、版本更新
	public final static String URL_VERSION_UPDATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=version";
	// 4、所有数据
	public final static String URL_ALL_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=test1";
	// 5、图片合成
	public final static String URL_IMAGE_CREATE = URL_SERVER_IP + "m=Home&c=Zbsq&a=start_zb";
	// 6、我的收藏
	public final static String URL_MY_KEEP_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=getCollectList";
	// 7、我的生成
	public final static String URL_MY_CREATE_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=history";
	// 8、收藏/取消收藏
	public final static String URL_ADD_KEEP_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=collect";
	//9.投稿接口
	public final static String URL_TOU_GAO = URL_SERVER_IP + "m=Home&c=Zbsq&a=contribute";
	//10、修改用户信息
	public final static String URL_UPDATE_USER = URL_SERVER_IP + "m=Home&c=Zbsq&a=setInfo";
	//11、获取用户信息
	public final static String URL_GET_USER_MESSAGE = URL_SERVER_IP + "m=Home&c=Zbsq&a=getUserInfo";
	//12、获取热门搜索关键词
	public final static String URL_GET_HOT_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=hot";
	//13、搜索的数据
	public final static String URL_SEARCH_DATA = URL_SERVER_IP + "m=Home&c=Zbsq&a=sreach";
	//14、所有斗图的数据
	public final static String URL_ALL_FIGHT_DATA = URL_SERVER_IP + "m=Home&c=Doutu&a=index";
	//15、文字处理
	public final static String URL_WORD_CREATE_DATA = URL_SERVER_IP + "m=Home&c=Doutu&a=viewFonts";
	//16、热门词列表
	public final static String URL_HOT_WORD_LIST_DATA = URL_SERVER_IP + "m=Home&c=Doutu&a=getHotWord";
	//17、文字处理
	public final static String URL_WORD_IMG_DELETE_DATA = URL_SERVER_IP + "m=Home&c=Doutu&a=delFile";
	//18、根据最大的ID，查询比这个ID大的所有数据
	public final static String URL_ALL_DATA_BY_ID = URL_SERVER_IP + "m=Home&c=zbsq&a=getBigData";
	//19、支付接口
	public final static String URL_PAY = URL_SERVER_IP + "m=api&c=index&a=pay";
	//20、用户QQWEIXI登录
	public final static String URL_QX_LOGIN = URL_SERVER_IP + "m=api&c=index&a=qqwx_login";
	//21、用户素材购买状态
	public final static String URL_USER_SOURCE_STATE = URL_SERVER_IP + "m=api&c=index&a=user_source_state";
	//22、价格设置
	public final static String URL_PRICE = URL_SERVER_IP + "m=api&c=index&a=get_price";
	//23、分类列表
	public final static String CATEGORY_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getCateList";
	//24、Banner列表
	public final static String BANNER_LIST_URL = URL_SERVER_IP + "m=Home&c=zbsq&a=getSlideMore";
	//25、帖子列表
	public final static String NOTE_LIST_URL = URL_SERVER_IP + "m=Api&c=note&a=notelist";
}
