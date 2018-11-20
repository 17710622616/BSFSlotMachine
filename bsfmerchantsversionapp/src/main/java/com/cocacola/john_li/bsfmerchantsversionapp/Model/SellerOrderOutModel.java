package com.cocacola.john_li.bsfmerchantsversionapp.Model;

import java.util.List;

/**
 * Created by John on 20/11/2018.
 */

public class SellerOrderOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":5,"data":[{"id":5,"orderNo":"X201811071348151300001","userId":30,"sellerId":1,"chargeId":4,"chargeRemark":"套餐A","totalAmount":10,"moneyBack":0,"payAmount":10,"orderStatus":1,"remark":"","payType":0,"isDelete":0,"createTime":1541569695000,"updateTime":1541569695000,"synTradeNo":"X201811071348151300001","asynTradeNo":"X201811071348151300001","currency":"CNY","exchange":"0.8888","exchangeAmountPay":8.89,"sellerLogo":"http://xxxxx.jpg","sellerName":"平台自营"}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * totalCount : 5
         * data : [{"id":5,"orderNo":"X201811071348151300001","userId":30,"sellerId":1,"chargeId":4,"chargeRemark":"套餐A","totalAmount":10,"moneyBack":0,"payAmount":10,"orderStatus":1,"remark":"","payType":0,"isDelete":0,"createTime":1541569695000,"updateTime":1541569695000,"synTradeNo":"X201811071348151300001","asynTradeNo":"X201811071348151300001","currency":"CNY","exchange":"0.8888","exchangeAmountPay":8.89,"sellerLogo":"http://xxxxx.jpg","sellerName":"平台自营"}]
         */

        private int totalCount;
        private List<SellerOrderModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<SellerOrderModel> getData() {
            return data;
        }

        public void setData(List<SellerOrderModel> data) {
            this.data = data;
        }

        public static class SellerOrderModel {
            /**
             * id : 5
             * orderNo : X201811071348151300001
             * userId : 30
             * sellerId : 1
             * chargeId : 4
             * chargeRemark : 套餐A
             * totalAmount : 10
             * moneyBack : 0
             * payAmount : 10
             * orderStatus : 1
             * remark :
             * payType : 0
             * isDelete : 0
             * createTime : 1541569695000
             * updateTime : 1541569695000
             * synTradeNo : X201811071348151300001
             * asynTradeNo : X201811071348151300001
             * currency : CNY
             * exchange : 0.8888
             * exchangeAmountPay : 8.89
             * sellerLogo : http://xxxxx.jpg
             * sellerName : 平台自营
             */

            private int id;
            private String orderNo;
            private int userId;
            private int sellerId;
            private int chargeId;
            private String chargeRemark;
            private int totalAmount;
            private int moneyBack;
            private int payAmount;
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

            public int getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(int totalAmount) {
                this.totalAmount = totalAmount;
            }

            public int getMoneyBack() {
                return moneyBack;
            }

            public void setMoneyBack(int moneyBack) {
                this.moneyBack = moneyBack;
            }

            public int getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(int payAmount) {
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
    }
}
