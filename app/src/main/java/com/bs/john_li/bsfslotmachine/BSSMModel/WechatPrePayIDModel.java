package com.bs.john_li.bsfslotmachine.BSSMModel;

import com.google.gson.annotations.SerializedName;

/**
 * 微信预支付信息
 * Created by John_Li on 21/6/2018.
 */

public class WechatPrePayIDModel {
    /**
     * code : 200
     * msg : null
     * data : {"appid":"wxa78767024378b134","noncestr":"x8zpyvfgej738enx","package":"Sign=WXPay","partnerid":"1507974481","prepayid":"wx2023324919430105e81333084291839935","sign":"F0298CA9E7E65C217547B5A55727CDC8","timestamp":"1529508788"}
     */

    private int code;
    private Object msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * appid : wxa78767024378b134
         * noncestr : x8zpyvfgej738enx
         * package : Sign=WXPay
         * partnerid : 1507974481
         * prepayid : wx2023324919430105e81333084291839935
         * sign : F0298CA9E7E65C217547B5A55727CDC8
         * timestamp : 1529508788
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
