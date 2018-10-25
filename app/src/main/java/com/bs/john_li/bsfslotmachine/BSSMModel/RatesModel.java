package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John_Li on 25/10/2018.
 */

public class RatesModel {
    /**
     * code : 200
     * msg :
     * data : {"carType":1,"timeLimit":4,"hourCost":1,"pillarColor":"yellow","areaCode":"FST","noVipHoursPay":"5"}
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
         * carType : 1
         * timeLimit : 4
         * hourCost : 1
         * pillarColor : yellow
         * areaCode : FST
         * noVipHoursPay : 5
         */

        private int carType;
        private int timeLimit;
        private int hourCost;
        private String pillarColor;
        private String areaCode;
        private String noVipHoursPay;

        public int getCarType() {
            return carType;
        }

        public void setCarType(int carType) {
            this.carType = carType;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public int getHourCost() {
            return hourCost;
        }

        public void setHourCost(int hourCost) {
            this.hourCost = hourCost;
        }

        public String getPillarColor() {
            return pillarColor;
        }

        public void setPillarColor(String pillarColor) {
            this.pillarColor = pillarColor;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getNoVipHoursPay() {
            return noVipHoursPay;
        }

        public void setNoVipHoursPay(String noVipHoursPay) {
            this.noVipHoursPay = noVipHoursPay;
        }
    }
}
