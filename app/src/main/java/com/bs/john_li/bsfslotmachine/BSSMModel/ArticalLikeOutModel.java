package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 點讚的接口
 * Created by John_Li on 19/4/2018.
 */

public class ArticalLikeOutModel {
    /**
     * code : 200
     * msg :
     * data : {"status":true}
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
         * status : true
         */

        private boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
