package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John_Li on 25/2/2019.
 */

public class GiveCouponOutModel {
    /**
     * code : 200
     * msg :
     * data : {"couponId":36,"couponName":"代入咪表订单优惠券","couponValue":3,"minAmount":20,"dayUseStartTime":"08:00:00","dayUseEndTime":"20:00:00","useStartTime":123456789,"useEndTime":123456789,"type":1,"remark":"系统赠送会员咪表代金券","status":0,"ifVip":0}
     */
    private int code;
    private String msg;
    private GiveCounponModel data;

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

    public GiveCounponModel getData() {
        return data;
    }

    public void setData(GiveCounponModel data) {
        this.data = data;
    }

    public static class GiveCounponModel {
        /**
         * couponId : 36
         * couponName : 代入咪表订单优惠券
         * couponValue : 3
         * minAmount : 20
         * dayUseStartTime : 08:00:00
         * dayUseEndTime : 20:00:00
         * useStartTime : 123456789
         * useEndTime : 123456789
         * type : 1
         * remark : 系统赠送会员咪表代金券
         * status : 0
         * ifVip : 0
         */

        private int couponId;
        private String couponName;
        private int couponValue;
        private int minAmount;
        private String dayUseStartTime;
        private String dayUseEndTime;
        private long useStartTime;
        private long useEndTime;
        private int type;
        private String remark;
        private int status;
        private int ifVip;

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public int getCouponValue() {
            return couponValue;
        }

        public void setCouponValue(int couponValue) {
            this.couponValue = couponValue;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(int minAmount) {
            this.minAmount = minAmount;
        }

        public String getDayUseStartTime() {
            return dayUseStartTime;
        }

        public void setDayUseStartTime(String dayUseStartTime) {
            this.dayUseStartTime = dayUseStartTime;
        }

        public String getDayUseEndTime() {
            return dayUseEndTime;
        }

        public void setDayUseEndTime(String dayUseEndTime) {
            this.dayUseEndTime = dayUseEndTime;
        }

        public long getUseStartTime() {
            return useStartTime;
        }

        public void setUseStartTime(long useStartTime) {
            this.useStartTime = useStartTime;
        }

        public long getUseEndTime() {
            return useEndTime;
        }

        public void setUseEndTime(long useEndTime) {
            this.useEndTime = useEndTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIfVip() {
            return ifVip;
        }

        public void setIfVip(int ifVip) {
            this.ifVip = ifVip;
        }
    }
}
