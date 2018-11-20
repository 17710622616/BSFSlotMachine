package com.cocacola.john_li.bsfmerchantsversionapp.Model;

import java.util.List;

/**
 * Created by John on 18/11/2018.
 */

public class SellerOrderDetialOutModel {
    /**
     * code : 200
     * msg :
     * data : {"seller":{"id":1,"sellerName":"平台自营","sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":10,"orderCount":444,"orderCountOver":1,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.533975,"latitude":22.186335,"address":"珠海","phone":"9999","sellerType":2,"pageView":0,"businessHours":"9:00-16:00","meter":""},"sellerOrder":{"id":6,"orderNo":"X201811121116549590001","userId":30,"sellerId":1,"chargeId":4,"chargeRemark":"套餐A","totalAmount":10,"moneyBack":0,"payAmount":10,"orderStatus":1,"remark":"","payType":0,"isDelete":0,"createTime":1542267942000,"updateTime":1542267942000,"synTradeNo":"","asynTradeNo":"","currency":"CNY","exchange":"0.8916","exchangeAmountPay":8.92,"exprieTime":1541142398000,"finishedTime":1543045539000,"sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg","sellerName":"平台自营"},"couponList":[{"id":2,"orderNo":"X201811151646339850001","couponCode":"168cee74","status":0,"createTime":1542271594000,"updateTime":1542271594000},{"id":3,"orderNo":"X201811151646339850001","couponCode":"168cee83","status":0,"createTime":1542271594000,"updateTime":null},{"id":4,"orderNo":"X201811151646339850001","couponCode":"168cee8e","status":0,"createTime":1542271594000,"updateTime":null}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * seller : {"id":1,"sellerName":"平台自营","sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":10,"orderCount":444,"orderCountOver":1,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.533975,"latitude":22.186335,"address":"珠海","phone":"9999","sellerType":2,"pageView":0,"businessHours":"9:00-16:00","meter":""}
         * sellerOrder : {"id":6,"orderNo":"X201811121116549590001","userId":30,"sellerId":1,"chargeId":4,"chargeRemark":"套餐A","totalAmount":10,"moneyBack":0,"payAmount":10,"orderStatus":1,"remark":"","payType":0,"isDelete":0,"createTime":1542267942000,"updateTime":1542267942000,"synTradeNo":"","asynTradeNo":"","currency":"CNY","exchange":"0.8916","exchangeAmountPay":8.92,"exprieTime":1541142398000,"finishedTime":1543045539000,"sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg","sellerName":"平台自营"}
         * couponList : [{"id":2,"orderNo":"X201811151646339850001","couponCode":"168cee74","status":0,"createTime":1542271594000,"updateTime":1542271594000},{"id":3,"orderNo":"X201811151646339850001","couponCode":"168cee83","status":0,"createTime":1542271594000,"updateTime":null},{"id":4,"orderNo":"X201811151646339850001","couponCode":"168cee8e","status":0,"createTime":1542271594000,"updateTime":null}]
         */

        private SellerBean seller;
        private SellerOrderBean sellerOrder;
        private List<CouponListBean> couponList;

        public SellerBean getSeller() {
            return seller;
        }

        public void setSeller(SellerBean seller) {
            this.seller = seller;
        }

        public SellerOrderBean getSellerOrder() {
            return sellerOrder;
        }

        public void setSellerOrder(SellerOrderBean sellerOrder) {
            this.sellerOrder = sellerOrder;
        }

        public List<CouponListBean> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<CouponListBean> couponList) {
            this.couponList = couponList;
        }

        public static class SellerBean {
            /**
             * id : 1
             * sellerName : 平台自营
             * sellerLogo : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg
             * scoreService : 4.0
             * productNumber : 21
             * createTime : 1456910927000
             * saleMoney : 10
             * orderCount : 444
             * orderCountOver : 1
             * sellerDes : 自营
             * status : 0
             * acount : admin
             * password :
             * longitude : 113.533975
             * latitude : 22.186335
             * address : 珠海
             * phone : 9999
             * sellerType : 2
             * pageView : 0
             * businessHours : 9:00-16:00
             * meter :
             */

            private int id;
            private String sellerName;
            private String sellerLogo;
            private String scoreService;
            private int productNumber;
            private long createTime;
            private int saleMoney;
            private int orderCount;
            private int orderCountOver;
            private String sellerDes;
            private int status;
            private String acount;
            private String password;
            private double longitude;
            private double latitude;
            private String address;
            private String phone;
            private int sellerType;
            private int pageView;
            private String businessHours;
            private String meter;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }

            public String getSellerLogo() {
                return sellerLogo;
            }

            public void setSellerLogo(String sellerLogo) {
                this.sellerLogo = sellerLogo;
            }

            public String getScoreService() {
                return scoreService;
            }

            public void setScoreService(String scoreService) {
                this.scoreService = scoreService;
            }

            public int getProductNumber() {
                return productNumber;
            }

            public void setProductNumber(int productNumber) {
                this.productNumber = productNumber;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getSaleMoney() {
                return saleMoney;
            }

            public void setSaleMoney(int saleMoney) {
                this.saleMoney = saleMoney;
            }

            public int getOrderCount() {
                return orderCount;
            }

            public void setOrderCount(int orderCount) {
                this.orderCount = orderCount;
            }

            public int getOrderCountOver() {
                return orderCountOver;
            }

            public void setOrderCountOver(int orderCountOver) {
                this.orderCountOver = orderCountOver;
            }

            public String getSellerDes() {
                return sellerDes;
            }

            public void setSellerDes(String sellerDes) {
                this.sellerDes = sellerDes;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAcount() {
                return acount;
            }

            public void setAcount(String acount) {
                this.acount = acount;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getSellerType() {
                return sellerType;
            }

            public void setSellerType(int sellerType) {
                this.sellerType = sellerType;
            }

            public int getPageView() {
                return pageView;
            }

            public void setPageView(int pageView) {
                this.pageView = pageView;
            }

            public String getBusinessHours() {
                return businessHours;
            }

            public void setBusinessHours(String businessHours) {
                this.businessHours = businessHours;
            }

            public String getMeter() {
                return meter;
            }

            public void setMeter(String meter) {
                this.meter = meter;
            }
        }

        public static class SellerOrderBean {
            /**
             * id : 6
             * orderNo : X201811121116549590001
             * userId : 30
             * sellerId : 1
             * chargeId : 4
             * chargeRemark : 套餐A
             * totalAmount : 10.0
             * moneyBack : 0.0
             * payAmount : 10.0
             * orderStatus : 1
             * remark :
             * payType : 0
             * isDelete : 0
             * createTime : 1542267942000
             * updateTime : 1542267942000
             * synTradeNo :
             * asynTradeNo :
             * currency : CNY
             * exchange : 0.8916
             * exchangeAmountPay : 8.92
             * exprieTime : 1541142398000
             * finishedTime : 1543045539000
             * sellerLogo : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg
             * sellerName : 平台自营
             */

            private int id;
            private String orderNo;
            private int userId;
            private int sellerId;
            private int chargeId;
            private String chargeRemark;
            private double totalAmount;
            private double moneyBack;
            private double payAmount;
            private int orderStatus;
            private String remark;
            private int payType;
            private int isDelete;
            private long createTime;
            private long updateTime;
            private String synTradeNo;
            private String asynTradeNo;
            private String currency;
            private String exchange;
            private double exchangeAmountPay;
            private long exprieTime;
            private long finishedTime;
            private String sellerLogo;
            private String sellerName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getSellerId() {
                return sellerId;
            }

            public void setSellerId(int sellerId) {
                this.sellerId = sellerId;
            }

            public int getChargeId() {
                return chargeId;
            }

            public void setChargeId(int chargeId) {
                this.chargeId = chargeId;
            }

            public String getChargeRemark() {
                return chargeRemark;
            }

            public void setChargeRemark(String chargeRemark) {
                this.chargeRemark = chargeRemark;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getMoneyBack() {
                return moneyBack;
            }

            public void setMoneyBack(double moneyBack) {
                this.moneyBack = moneyBack;
            }

            public double getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(double payAmount) {
                this.payAmount = payAmount;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getSynTradeNo() {
                return synTradeNo;
            }

            public void setSynTradeNo(String synTradeNo) {
                this.synTradeNo = synTradeNo;
            }

            public String getAsynTradeNo() {
                return asynTradeNo;
            }

            public void setAsynTradeNo(String asynTradeNo) {
                this.asynTradeNo = asynTradeNo;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getExchange() {
                return exchange;
            }

            public void setExchange(String exchange) {
                this.exchange = exchange;
            }

            public double getExchangeAmountPay() {
                return exchangeAmountPay;
            }

            public void setExchangeAmountPay(double exchangeAmountPay) {
                this.exchangeAmountPay = exchangeAmountPay;
            }

            public long getExprieTime() {
                return exprieTime;
            }

            public void setExprieTime(long exprieTime) {
                this.exprieTime = exprieTime;
            }

            public long getFinishedTime() {
                return finishedTime;
            }

            public void setFinishedTime(long finishedTime) {
                this.finishedTime = finishedTime;
            }

            public String getSellerLogo() {
                return sellerLogo;
            }

            public void setSellerLogo(String sellerLogo) {
                this.sellerLogo = sellerLogo;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }
        }

        public static class CouponListBean {
            /**
             * id : 2
             * orderNo : X201811151646339850001
             * couponCode : 168cee74
             * status : 0
             * createTime : 1542271594000
             * updateTime : 1542271594000
             */

            private int id;
            private String orderNo;
            private String couponCode;
            private int status;
            private long createTime;
            private long updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getCouponCode() {
                return couponCode;
            }

            public void setCouponCode(String couponCode) {
                this.couponCode = couponCode;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
