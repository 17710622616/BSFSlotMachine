package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John on 18/10/2017.
 */

public class MaxAmountModel {

    /**
     * code : 200
     * msg :
     * data : {"hourCost":2,"amountLimit":0}
     */

    private int code;
    private String msg;
    private MaxModel data;

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

    public MaxModel getData() {
        return data;
    }

    public void setData(MaxModel data) {
        this.data = data;
    }

    public static class MaxModel {
        /**
         * hourCost : 2
         * amountLimit : 0
         */

        private int hourCost;
        private int amountLimit;

        public int getHourCost() {
            return hourCost;
        }

        public void setHourCost(int hourCost) {
            this.hourCost = hourCost;
        }

        public int getAmountLimit() {
            return amountLimit;
        }

        public void setAmountLimit(int amountLimit) {
            this.amountLimit = amountLimit;
        }
    }
}
