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
    // add car for result
    public static int ADD_CAR_RESULT = 4;
    // 咪錶不存在
    public final static String SLOT_MACHINE_NOT_EXIST = "NOT_EXIST";
    // 咪錶搜索结果
    public final static String SLOT_MACHINE_FROM_SEARCH = "FROM_SEARCH";
    // 咪錶存在
    public final static String SLOT_MACHINE_EXIST = "LOCATION_EXIST";

    // 測試的IP地址
    public final static String BASE_URL = "http://47.94.254.169:8080/parkingman-web/";
    // 用戶登錄的API
    public final static String USER_LOGIN = "user/login";
    // 用戶註冊的API
    public final static String USER_RIGISTER = "reg/submit";
    // 獲取驗證碼
    public final static String GET_VERIFICATION_CODE = "reg/getVerifyCode";
    // 发送位置的API
    public final static String USER_LOCATION = "soltMachine/byDistinct";
    // 搜索咪錶編號的API
    public final static String SEARCH_SLOT_MACHINE = "soltMachine/byKey";
    // 添加車輛
    public final static String ADD_CAR = "car/add?token=";
}
