package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 22/10/2017.
 */

public class CarRechargeWayListModel {

    /**
     * code : 200
     * msg :
     * data : [{"id":1,"monthNum":1,"description":"月费:300","amount":300,"refundAmount":0,"isDelete":"","createTime":"","updateTime":""},{"id":4,"monthNum":12,"description":"年费:3600(300*12)返现720即30%","amount":3600,"refundAmount":720,"isDelete":"","createTime":"","updateTime":""}]
     */

    private int code;
    private String msg;
    private List<CarRechargeWayModel> data;

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

    public List<CarRechargeWayModel> getData() {
        return data;
    }

    public void setData(List<CarRechargeWayModel> data) {
        this.data = data;
    }

    public static class CarRechargeWayModel {
        /**
         * id : 1
         * monthNum : 1
         * description : 月费:300
         * amount : 300
         * refundAmount : 0
         * isDelete :
         * createTime :
         * updateTime :
         */

        private int id;
        private int monthNum;
        private String description;
        private int amount;
        private int refundAmount;
        private String isDelete;
        private String createTime;
        private String updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMonthNum() {
            return monthNum;
        }

        public void setMonthNum(int monthNum) {
            this.monthNum = monthNum;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(int refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
