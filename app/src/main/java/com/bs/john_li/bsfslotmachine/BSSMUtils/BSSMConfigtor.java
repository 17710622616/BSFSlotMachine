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
    // 运行时权限
    public static final int MY_PERMISSION_REQUEST_CODE = 10000;
    // 聚合數據APPKEY
    public final static String JUHE_APPKEY = "9dfd4b86e08257c036ef4e5a6558882a";
    // OSS請求token的接口
    public static final String OSS_TOKEN = "oss/stsAuthorize";
    // OSS的应用服务器回調地址
    public static final String OSS_SERVER_CALLBACK_ADDRESS = "https://test-pic-666.oss-cn-hongkong.aliyuncs.com/";
    // OSS的BucketName
    public static final String BucketName = "test-pic-666";
    // OSS的BucketName
    public static final String END_POINT = "oss-cn-hongkong.aliyuncs.com";
    // 咪錶不存在
    public final static String SLOT_MACHINE_NOT_EXIST = "NOT_EXIST";
    // 咪錶搜索结果
    public final static String SLOT_MACHINE_FROM_SEARCH = "FROM_SEARCH";
    // 咪錶存在
    public final static String SLOT_MACHINE_EXIST = "LOCATION_EXIST";
    // 微信APP_ID
    public final static String WECHAT_APPID = "wxa78767024378b134";
    // sharesdk的appkey
    public final static String SHARESDK_APP_KEY = "24f00c70bb6c2";
    // sharesdk的secret
    public final static String SHARESDK_APP_SECRET = "e3860e99759647b3ad5e17b2a5c09de6";

    // 正式的域名地址
    public final static String BASE_URL = "https://www.bosoftmacao.cn/parkingman-web/";
    // 正式的IP地址
    //public final static String BASE_URL = "http://120.77.84.40/parkingman-web/";
    // 測試的IP地址
    //public final static String BASE_URL = "http://47.75.5.50/parkingman-web/";
    // 測試的IP地址
    //public final static String BASE_URL = "http://47.94.254.169:8080/parkingman-web/";
    // 獲取驗證碼
    public final static String GET_SPLASH = "indexBanner/index";
    // 獲取驗證碼
    public final static String GET_VERIFICATION_CODE = "reg/getVerifyCode";
    // 用戶登錄的API
    public final static String USER_LOGIN = "user/login";
    // 用戶註冊的API
    public final static String USER_REGISTER = "reg/submit";
    // 用戶忘記密碼的API
    public final static String USER_FORGET_PW = "reg/chgpwd";
    // 獲取用戶信息
    public final static String GET_USER_INFO = "user/getuser?token=";
    // 更新用戶信息
    public final static String UPDATE_USER_INFO = "user/chguser?token=";
    // 更新用戶頭像
    public final static String UPDATE_USER_HEAD_IMG = "user/chgimg?img=";
    // 獲取用戶是否有支付密码
    public final static String GET_USER_HAS_PAY_PW = "user/ispaypwd?token=";
    // 修改支付密码
    public final static String CHANGE_USER_PAY_PW = "user/chgpaypwd?token=";
    // 創建支付密码
    public final static String CREATE_USER_PAY_PW = "user/addpaypwd?token=";

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
    // 提交已知咪錶訂單
    public final static String SUBMIT_ORDER_SLOT_MACHINE_EXIST = "order/newslotOrder?token=";
    // 提交未知咪錶訂單
    public final static String SUBMIT_ORDER_SLOT_MACHINE_UNKOWN = "order/newUnkownSlotOrder?token=";
    // 提交取消訂單
    public final static String CANCELLATION_OF_ORDER = "order/cancelOrder?token=";
    // 獲取收費標準
    public final static String GET_ORDER_RATES = "systemProfile/machineProfile?token=";
    // 獲取已知咪表最大金额
    public final static String GET_MAX_AMOUNT_BY_SLOT_MACHINE = "soltMachine/chargeLimit";
    // 獲取未知咪表最大金额
    public final static String GET_MAX_AMOUNT_SLOT_MACHINE_UNKOWN = "soltMachine/chargeUnkownLimit";
    // 錢包支付的接口
    public final static String POST_WALLET_PAY = "pay/walletPay?token=";
    // 錢包常見問題的接口
    public final static String GET_WALLET_FAQ_WEB = "systemProfile/walletIndex";
    // 微信支付获取prepay_id的接口
    public final static String POST_WECHAT_PAY_PRE_PAY_ID = "pay/weixinTrade?token=";
    // 支付寶支付获取prepay_id的接口
    public final static String POST_APLIPAY_ORDER_INFO = "pay/alipayTrade?token=";
    // 信用卡支付获取prepay_id的接口
    public final static String POST_CREDIT_CARD_ORDER_INFO = "payPalPay/pay?token=";
    // 錢包餘額的接口
    public final static String GET_WALLET_BALANCE = "pay/getBalance?token=";
    // 錢包餘額的接口
    public final static String GET_WALLET_RECORD = "pay/getUserWalletRecord?token=";
    // 钱包充值订单的接口
    public final static String POST_CHARGE_ORDER = "order/chargeOrder?token=";
    // 钱包提現手續費的接口
    public final static String GET_SERVICE_CHARGE = "systemProfile/getWithDrawRate?token=";
    // 钱包提現订单的接口
    public final static String POST_WITH_DRAWAL = "userCash/createWithDraw?token=";
    // 钱包提現記錄的接口
    public final static String GET_WITH_DRAWAL_RECORDE = "userCash/getWithDrawList?token=";
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
    // 点赞接口
    public final static String ARTICAL_ISLIKE = "content/isLike?token=";
    // 点赞接口
    public final static String ARTICAL_LIKE = "content/like?";
    // 取消点赞接口
    public final static String ARTICAL_UNLIKE = "content/unlike?";
    // 獲取訂單列表
    public final static String GET_ORDER_LIST = "order/getUserOrderList?token=";
    // 獲取首頁贈送優惠券
    public final static String GET_GIVE_COUPON = "coupon/giveCoupon?token=";
    // 獲取用戶可用優惠券數量
    public final static String GET_USE_COUPON_NUMBER = "coupon/couponCount?token=";
    // 獲取優惠券列表
    public final static String GET_DISCOUNT_LIST = "coupon/listByPage?token=";
    //軟件版本號對比地址
    public static final String CHECK_VERSION= "AppVersion/selectAppVersion";
    //提交意見反饋的接口
    public static final String SUBMIT_OPINION= "feedback/submit?token=";
    // 教程的接口
    public final static String GET_COURSE_WEB = "systemProfile/teach";
    // 分享的接口
    public final static String GET_SHARE_WEB = "share/index?token=";
    /*-------------------------------------------------汽車服務-----------------------------------------------------------------------------------------*/
    //商家列表的接口
    public static final String GET_SELLER_LIST= "seller/getSellerList";
    //推薦商家列表的接口
    public static final String GET_HOT_SELLER_LIST= "seller/hotSellerList";
    //商家套餐列表的接口
    public static final String GET_MERCHART_SET= "seller/selectSellerById";
    //洗車訂單下單的接口
    public static final String SUBMIT_CAR_WASH_ORDER = "sellerOrder/createOrder?token=";
    //洗車訂單微信的接口
    public static final String POST_CW_WECHAT_PAY_PRE_PAY_ID = "sellerPay/weixinTrade?token=";
    //洗車訂單支付寶的接口
    public static final String POST_CW_ALIPAY_PAY_PRE_PAY_ID = "sellerPay/alipayTrade?token=";
    //洗車訂單支付寶的接口
    public static final String POST_CW_WALLET_PAY_PRE_PAY_ID = "sellerPay/walletPay?token=";
    //洗車訂單列表的接口
    public static final String GET_CW_ORDER_LIST = "sellerOrder/getUserOrderList?token=";
    //洗車訂單詳情的接口
    public static final String GET_CW_ORDER_DETIAL = "sellerOrder/orderDetial?token=";
    //取消洗車訂單的接口
    public static final String POST_REFUND_CAR_WASH_ORDER = "sellerOrder/withDrawApply?token=";
    //获取轮播图的接口
    public static final String GET_SECONDE_CAR_SIDESHOW = "seller/selectSellerCarouselByType";
    //获取热门车辆的接口
    public static final String GET_HOT_CAR = "sellerCar/hotCarlList";
    //获取二手車车辆的接口
    public static final String GET_OLD_CAR_LIST = "sellerCar/sellerCarlList?token=";
    //获取用戶二手車车辆列表的接口
    public static final String GET_OWN_OLD_CAR_LIST = "sellerCar/myCarlList?token=";
    //获取用戶收藏二手車车辆列表的接口
    public static final String GET_COLLECT_OLD_CAR_LIST = "sellerCarCollection/getUserSellerCarCollectionList?token=";
    //二手車點讚的接口
    public static final String COLLECTION_CAR = "sellerCarCollection/add?token=";
    //取消二手車點讚的接口
    public static final String UNCOLLECTION_CAR = "sellerCarCollection/remove?token=";
    //获取車輛品牌列表的接口
    public static final String GET_CAR_BRAND_LIST = "sellerCar/carBrandList?token=";
    //提交二手車的接口
    public static final String SUBMIT_OLD_CAR = "sellerCar/addCar?token=";
    //獲取汽車詳情的接口
    public static final String GET_CAR_DETIAL = "sellerCar/carDetial";
    //獲取商家詳情的接口
    public static final String GET_SELLER_DETIAL = "seller/sellerDetial";
    //獲取商家汽車零件列表的接口
    public static final String GET_SELLER_PARTS = "sellerParts/partslList";
    //獲取商家汽車零件套餐詳情的接口
    public static final String GET_PARTS_SET_DETIAL = "sellerParts/partsDetial";
    //创建汽车零件订单的接口
    public static final String SUBMIT_PARTS_ORDER = "sellerOrder/createPartsOrder?token=";
}
