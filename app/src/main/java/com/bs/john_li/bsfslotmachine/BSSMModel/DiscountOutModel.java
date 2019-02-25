package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 25/2/2019.
 */

public class DiscountOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":2,"data":[{"couponId":1,"couponName":"会员邀请代金券","couponValue":10,"minAmount":20,"dayUseStartTime":"09:00:00","dayUseEndTime":"20:00:00","useStartTime":1469871324000,"useEndTime":1505404799000,"type":1,"remark":"","status":0},{"couponId":1,"couponName":"会员邀请代金券","couponValue":10,"minAmount":20,"dayUseStartTime":"09:00:00","dayUseEndTime":"20:00:00","useStartTime":1469871324000,"useEndTime":1505404799000,"type":1,"remark":"满MOP20可用","status":0}]}
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
         * totalCount : 2
         * data : [{"couponId":1,"couponName":"会员邀请代金券","couponValue":10,"minAmount":20,"dayUseStartTime":"09:00:00","dayUseEndTime":"20:00:00","useStartTime":1469871324000,"useEndTime":1505404799000,"type":1,"remark":"","status":0},{"couponId":1,"couponName":"会员邀请代金券","couponValue":10,"minAmount":20,"dayUseStartTime":"09:00:00","dayUseEndTime":"20:00:00","useStartTime":1469871324000,"useEndTime":1505404799000,"type":1,"remark":"满MOP20可用","status":0}]
         */

        private int totalCount;
        private List<DiscountModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<DiscountModel> getData() {
            return data;
        }

        public void setData(List<DiscountModel> data) {
            this.data = data;
        }

        public static class DiscountModel {
            /**
             * couponId : 1
             * couponName : 会员邀请代金券
             * couponValue : 10
             * minAmount : 20
             * dayUseStartTime : 09:00:00
             * dayUseEndTime : 20:00:00
             * useStartTime : 1469871324000
             * useEndTime : 1505404799000
             * type : 1
             * remark :
             * status : 0
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
            private String remark2;
            private int status;

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

            public String getRemark2() {
                return remark2;
            }

            public void setRemark2(String remark2) {
                this.remark2 = remark2;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
