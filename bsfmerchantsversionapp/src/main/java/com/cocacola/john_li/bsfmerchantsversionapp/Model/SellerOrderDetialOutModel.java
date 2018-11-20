package com.cocacola.john_li.bsfmerchantsversionapp.Model;

/**
 * Created by John on 18/11/2018.
 */

public class SellerOrderDetialOutModel {
    /**
     * code : 200
     * msg :
     * data : {"id":26,"orderNo":"X201811161128278280001","userId":25,"sellerId":1,"chargeId":4,"chargeRemark":"套餐A","totalAmount":10,"moneyBack":0,"payAmount":10,"chargeAmount":10,"orderStatus":5,"remark":"X201811161128278280001","payType":3,"isDelete":0,"createTime":1542338925000,"updateTime":1542338925000,"synTradeNo":"X201811161128278280001","asynTradeNo":"X201811161128278280001","currency":"CNY","exchange":"0.8903","exchangeAmountPay":8.9,"exprieTime":1543420800000,"finishedTime":1543420800000,"sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car1A9F716CAEA64D6688329BB7169B0E0B.jpg","sellerName":"平台自营"}
     */

    private int code;
    private String msg;
    private SellerOrderDetialModel data;

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

    public SellerOrderDetialModel getData() {
        return data;
    }

    public void setData(SellerOrderDetialModel data) {
        this.data = data;
    }

    public static class SellerOrderDetialModel {
        /**
         * id : 26
         * orderNo : X201811161128278280001
         * userId : 25
         * sellerId : 1
         * chargeId : 4
         * chargeRemark : 套餐A
         * totalAmount : 10.0
         * moneyBack : 0.0
         * payAmount : 10.0
         * chargeAmount : 10.0
         * orderStatus : 5
         * remark : X201811161128278280001
         * payType : 3
         * isDelete : 0
         * createTime : 1542338925000
         * updateTime : 1542338925000
         * synTradeNo : X201811161128278280001
         * asynTradeNo : X201811161128278280001
         * currency : CNY
         * exchange : 0.8903
         * exchangeAmountPay : 8.9
         * exprieTime : 1543420800000
         * finishedTime : 1543420800000
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
        private double chargeAmount;
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

        public double getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(double chargeAmount) {
            this.chargeAmount = chargeAmount;
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
}
