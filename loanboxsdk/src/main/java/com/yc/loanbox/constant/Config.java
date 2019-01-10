package com.yc.loanbox.constant;

public class Config {
    public static final boolean DEBUG = false;

    private static String baseUrl = "http://u.wk990.com/api/";
    private static String debugBaseUrl = "http://u.wk990.com/api/";

    public static final String APPID = "?app_id=8";
    public static final String INIT_URL = getBaseUrl() + "index/init" + APPID;
    public static final String Index_URL = getBaseUrl() + "loan/index"+ APPID;
    public static final String TYPE_URL = getBaseUrl() + "loan/type_list"+ APPID;
    public static final String SORT_URL = getBaseUrl() + "loan/search_info"+ APPID;
    public static final String SEARCH_RESULT_URL = getBaseUrl() + "loan/search_result"+ APPID;
    public static final String YOU_LIKE_LIST_URL = getBaseUrl() + "loan/you_like_list"+ APPID;
    public static final String SET_LOAD_NUM_URL = getBaseUrl() + "loan/set_loan_num"+ APPID;
    public static final String SEND_CODE_URL = getBaseUrl() + "loan/send_code"+ APPID;
    public static final String LOGIN_URL = getBaseUrl() + "loan/login"+ APPID;
    public static final String NEW_LIST_URL = getBaseUrl() + "/loan/newentry_list" + APPID;
    public static final String NOTICE_URL = getBaseUrl() + "/loan/newentry_notice" + APPID;
    public static final String APP_INDEX_URL = getBaseUrl() + "/loan/app_index" + APPID;
    public static final String APP_INSTALL_URL = getBaseUrl() + "/loan/app_install_log" + APPID;


    public static final String TYPE97 = 97+"";  // 类型列表顶部消息
    public static final String TYPE98 = 98+"";  // 贷款大全顶部消息
    public static final String TYPE99 = 99+"";  // 首页 消息
    public static final String TYPE100 = 100+""; // 换一批
    public static final String TYPE101 = 101+""; // 今日火爆
    public static final String TYPE102 = 102+""; // 贷款大全
    public static final String TYPE103 = 103+""; // 猜你可贷
    public static final String TYPE104 = 104+""; // 浏览记录
    public static final String TYPE105 = 105+""; // 通知
    public static final String TYPE106 = 106+""; // 最新口子 最新
    public static final String TYPE107 = 107+""; // 最新口子 今日
    public static final String TYPE108 = 108+""; // 通知

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }
}
