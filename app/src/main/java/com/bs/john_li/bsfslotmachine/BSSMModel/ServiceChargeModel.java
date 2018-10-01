package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 提現手續費
 * Created by John on 30/9/2018.
 */

public class ServiceChargeModel {
    /**
     * code : 0
     * msg :
     * data : {"id":1,"fcategory":"WITHDRAW_RATE","fvalue":"3","fdesc":"提现手续费3%"}
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
         * id : 1
         * fcategory : WITHDRAW_RATE
         * fvalue : 3
         * fdesc : 提现手续费3%
         */

        private int id;
        private String fcategory;
        private String fvalue;
        private String fdesc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFcategory() {
            return fcategory;
        }

        public void setFcategory(String fcategory) {
            this.fcategory = fcategory;
        }

        public String getFvalue() {
            return fvalue;
        }

        public void setFvalue(String fvalue) {
            this.fvalue = fvalue;
        }

        public String getFdesc() {
            return fdesc;
        }

        public void setFdesc(String fdesc) {
            this.fdesc = fdesc;
        }
    }
}
