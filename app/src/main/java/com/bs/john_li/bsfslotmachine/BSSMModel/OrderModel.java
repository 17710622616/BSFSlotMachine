package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 订单内容
 * Created by John_Li on 23/11/2017.
 */

public class OrderModel {

    /**
     * code : 200
     * msg :
     * data : {"orderNo":"S201711052303060450001","createTime":1509894186076,"amount":"20","exchange":"0.8756","exchangeAmountPay":"17.01"}
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
         * orderNo : S201711052303060450001
         * createTime : 1509894186076
         * amount : 20
         * exchange : 0.8756
         * exchangeAmountPay : 17.01
         */

        private String orderNo;
        private long createTime;
        private String amount;
        private String exchange;
        private String exchangeAmountPay;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getExchangeAmountPay() {
            return exchangeAmountPay;
        }

        public void setExchangeAmountPay(String exchangeAmountPay) {
            this.exchangeAmountPay = exchangeAmountPay;
        }
    }
}
