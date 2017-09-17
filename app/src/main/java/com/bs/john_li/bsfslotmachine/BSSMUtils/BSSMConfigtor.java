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
    // Login for result
    public static int LOGIN_FOR_RESULT = 1;

    // 測試的IP地址
    public final static String BASE_URL = "http://47.94.254.169:8080/parkingman-web/";
    // 用戶登錄的API
    public final static String USER_LOGIN = "reg/submit";
    // 用戶登錄的API
    public final static String GET_VERIFICATION_CODE = "reg/getVerifyCode";
    // 发送位置的API
    public final static String USER_LOCATION = "soltMachine/byDistinct";
}
