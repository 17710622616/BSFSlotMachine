package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 20/6/2018.
 */

public class WalletRecordOutModel {

    /**
     * code : 200
     * msg : null
     * data : {"totalCount":2,"data":[{"userId":16,"amount":111,"flag":1,"createTime":1527005213000,"payType":2},{"userId":16,"amount":222,"flag":2,"createTime":1527005246000,"payType":3}]}
     */

    private int code;
    private Object msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * totalCount : 2
         * data : [{"userId":16,"amount":111,"flag":1,"createTime":1527005213000,"payType":2},{"userId":16,"amount":222,"flag":2,"createTime":1527005246000,"payType":3}]
         */

        private int totalCount;
        private List<WalletRecordModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<WalletRecordModel> getData() {
            return data;
        }

        public void setData(List<WalletRecordModel> data) {
            this.data = data;
        }

        public static class WalletRecordModel {
            /**
             * userId : 16
             * amount : 111
             * flag : 1
             * createTime : 1527005213000
             * payType : 2
             */

            private int userId;
            private double amount;
            private int flag;
            private long createTime;
            private int payType;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }
        }
    }
}
