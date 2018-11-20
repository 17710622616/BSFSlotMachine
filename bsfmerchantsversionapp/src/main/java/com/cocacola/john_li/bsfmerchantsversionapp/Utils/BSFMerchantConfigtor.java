package com.cocacola.john_li.bsfmerchantsversionapp.Utils;

/**
 * Created by John on 18/11/2018.
 */

public class BSFMerchantConfigtor {
    public final static int LOGIN_FOR_RQUEST = 1;
    // OSS請求token的接口
    public static final String OSS_TOKEN = "oss/stsAuthorize";
    // OSS的应用服务器回調地址
    public static final String OSS_SERVER_CALLBACK_ADDRESS = "https://test-pic-666.oss-cn-hongkong.aliyuncs.com/";
    // OSS的BucketName
    public static final String BucketName = "test-pic-666";
    // OSS的BucketName
    public static final String END_POINT = "oss-cn-hongkong.aliyuncs.com";

    // 正式的域名地址
    public final static String BASE_URL = "https://www.easydev.top/parkingman-web/";
    // 商家錄
    public final static String SELLER_LOGIN = "user/sellerlogin";
    // 商家扫码查看洗车订单详情
    public final static String SELLER_ORDER_DETIAL = "sellerOrder/sellerOrderDetial";
    // 商家扫码完成洗车订单
    public final static String SELLER_FINISH_ORDER = "sellerOrder/finishOrder";
    // 商家扫码完成洗车订单
    public final static String SELLER_ORDER_LIST = "sellerOrder/getSellerOrderList";
    // 商家套餐列表
    public final static String SELLER_SET_LIST = "sellerCharge/sellerChargeList";
    // 商家添加套餐
    public final static String SELLER_ADD_SET = "sellerCharge/addSellerCharge";
}
