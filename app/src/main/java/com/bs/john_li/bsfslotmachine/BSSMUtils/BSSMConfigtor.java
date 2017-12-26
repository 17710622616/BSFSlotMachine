package com.bs.john_li.bsfslotmachine.BSSMUtils;

/**
 * 配置文件
 * Created by John on 12/9/2017.
 */

public class BSSMConfigtor {
    // osTYpe
    public static String OS_TYPE = null;
    // osVersion
    public static String OS_VERSION = null;
    // request_code
    public static int REQUEST_CODE = 100;
    // Login for request
    public static int LOGIN_FOR_RQUEST = 1;
    // Login for result
    public static int LOGIN_FOR_RESULT = 2;
    // add car for request
    public static int ADD_CAR_RQUEST = 3;
    // update car for request
    public static int UPDATE_CAR_RQUEST = 5;
    // add car for result
    public static int ADD_CAR_RESULT = 4;
    // 聚合數據APPKEY
    public final static String JUHE_APPKEY = "9dfd4b86e08257c036ef4e5a6558882a";
    // OSS的accessKey
    public static final String accessKey = "LTAIGRQR9R5krVv2";
    // 你的scretKey
    public static final String screctKey = "lGYpQfXF1CuL8TrBvsjCXDorMBJ5eu";
    // OSS的BucketName
    public static final String BucketName = "parkingman";
    // OSS的BucketName
    public static final String END_POINT = "https://oss-cn-beijing.aliyuncs.com";
    // 咪錶不存在
    public final static String SLOT_MACHINE_NOT_EXIST = "NOT_EXIST";
    // 咪錶搜索结果
    public final static String SLOT_MACHINE_FROM_SEARCH = "FROM_SEARCH";
    // 咪錶存在
    public final static String SLOT_MACHINE_EXIST = "LOCATION_EXIST";

    // 測試的IP地址
    public final static String BASE_URL = "http://47.94.254.169:8080/parkingman-web/";
    // 獲取驗證碼
    public final static String GET_VERIFICATION_CODE = "reg/getVerifyCode";
    // 用戶登錄的API
    public final static String USER_LOGIN = "user/login";
    // 用戶註冊的API
    public final static String USER_REGISTER = "reg/submit";
    // 獲取用戶信息
    public final static String GET_USER_INFO = "user/getuser?token=";
    // 更新用戶信息
    public final static String UPDATE_USER_INFO = "/user/chguser?token=";
    // 发送位置的API
    public final static String USER_LOCATION = "soltMachine/byDistinct";
    // 搜索咪錶編號的API
    public final static String SEARCH_SLOT_MACHINE = "soltMachine/byKey";
    // 獲取車輛列表
    public final static String GET_CAR_LIST = "car/listByPage?token=";
    // 獲取已充值車輛列表
    public final static String GET_CAR_LIST_RECHARGE = "car/getUserPayCars?token=";
    // 添加車輛
    public final static String ADD_CAR = "car/add?token=";
    // 修改車輛
    public final static String UPDATE_CAR = "car/modify?token=";
    // 刪除車輛
    public final static String DELETE_CAR = "car/remove?token=";
    // 車輛充值價格的列表
    public final static String CAR_CAHRGE_WAY_LIST = "memberCharge/getAll";
    // 車輛充值價格的列表
    public final static String SUBMIT_CAR_CAHRGE_ORDER = "order/memberOrder?token=";
    // 獲取已充值車輛列表
    public final static String SUBMIT_ORDER_SLOT_MACHINE_EXIST = "order/slotOrder?token=";
    // 獲取已知咪表最大金额
    public final static String GET_MAX_AMOUNT_BY_SLOT_MACHINE = "soltMachine/chargeLimit";
    // 獲取未知咪表最大金额
    public final static String GET_MAX_AMOUNT_SLOT_MACHINE_UNKOWN = "soltMachine/chargeUnkownLimit";
    // 獲取帖文列表
    public final static String GET_CONTENTS = "content/getContentList?nextId=";
    // 獲取評論列表
    public final static String GET_COMMENTS = "content/getCommentList?id=";
    // 刪除帖文
    public final static String DELETE_COTENTS = "content/delContent?id=";
    // 登录的用户发布评论
    public final static String SUBMITE_COMMENT_LOGIN = "content/subcomment?token=";
    // 登录的人给指定的评论回复
    public final static String SUBMITE_REPLY_LOGIN = "content/reply?token=";
    // 游客发布评论
    public final static String SUBMITE_COMMENT_VISITOR = "content/visitorcomment";
    // 登录的人给指定的评论回复
    public final static String SUBMITE_REPLY_VISITOR = "content/visitorreply";
    // 發佈帖子
    public final static String PUBLISH_ARTICLE = "content/publish?token=";
}
