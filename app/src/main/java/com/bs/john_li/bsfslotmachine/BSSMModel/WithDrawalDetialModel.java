package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 30/9/2018.
 */

public class WithDrawalDetialModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":2,"data":[{"id":1408,"userId":30,"money":500,"createDate":1536914231000,"status":0,"type":1,"realName":"马大哈","phoneNumber":"18575618939","bankName":"中国银行","bankCardNo":"554122222447746611444","weixinAccount":"lizhijian1888","zfbAccount":"15555@122.com","rate":3,"realmoney":0},{"id":1409,"userId":30,"money":500,"createDate":1536914231000,"status":0,"type":1,"realName":"马大哈","phoneNumber":"18575618939","bankName":"中国银行","bankCardNo":"554122222447746611444","weixinAccount":"lizhijian1888","zfbAccount":"15555@122.com","rate":3,"realmoney":0}]}
     */

    private int code;
    private String msg;
    private WithDrawalDetialInsideModel data;

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

    public WithDrawalDetialInsideModel getData() {
        return data;
    }

    public void setData(WithDrawalDetialInsideModel data) {
        this.data = data;
    }

    public static class WithDrawalDetialInsideModel {
        /**
         * totalCount : 2
         * data : [{"id":1408,"userId":30,"money":500,"createDate":1536914231000,"status":0,"type":1,"realName":"马大哈","phoneNumber":"18575618939","bankName":"中国银行","bankCardNo":"554122222447746611444","weixinAccount":"lizhijian1888","zfbAccount":"15555@122.com","rate":3,"realmoney":0},{"id":1409,"userId":30,"money":500,"createDate":1536914231000,"status":0,"type":1,"realName":"马大哈","phoneNumber":"18575618939","bankName":"中国银行","bankCardNo":"554122222447746611444","weixinAccount":"lizhijian1888","zfbAccount":"15555@122.com","rate":3,"realmoney":0}]
         */

        private int totalCount;
        private List<DataBean> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 1408
             * userId : 30
             * money : 500
             * createDate : 1536914231000
             * status : 0
             * type : 1
             * realName : 马大哈
             * phoneNumber : 18575618939
             * bankName : 中国银行
             * bankCardNo : 554122222447746611444
             * weixinAccount : lizhijian1888
             * zfbAccount : 15555@122.com
             * rate : 3
             * realmoney : 0
             */

            private int id;
            private int userId;
            private double money;
            private long createDate;
            private int status;
            private int type;
            private String realName;
            private String phoneNumber;
            private String bankName;
            private String bankCardNo;
            private String weixinAccount;
            private String zfbAccount;
            private int rate;
            private int realmoney;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public String getBankCardNo() {
                return bankCardNo;
            }

            public void setBankCardNo(String bankCardNo) {
                this.bankCardNo = bankCardNo;
            }

            public String getWeixinAccount() {
                return weixinAccount;
            }

            public void setWeixinAccount(String weixinAccount) {
                this.weixinAccount = weixinAccount;
            }

            public String getZfbAccount() {
                return zfbAccount;
            }

            public void setZfbAccount(String zfbAccount) {
                this.zfbAccount = zfbAccount;
            }

            public int getRate() {
                return rate;
            }

            public void setRate(int rate) {
                this.rate = rate;
            }

            public int getRealmoney() {
                return realmoney;
            }

            public void setRealmoney(int realmoney) {
                this.realmoney = realmoney;
            }
        }
    }
}
