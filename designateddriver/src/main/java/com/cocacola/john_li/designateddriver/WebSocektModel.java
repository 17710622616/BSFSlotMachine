package com.cocacola.john_li.designateddriver;

/**
 * Created by John_Li on 14/3/2019.
 */

public class WebSocektModel {
    /**
     * code : 0
     * msg :
     * data : {"id":1,"fcategory":"WEB'SOCK'ET","fvalue":"0","fdesc":"112.74.52.98:3333"}
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
         * fcategory : WEB'SOCK'ET
         * fvalue : 0
         * fdesc : 112.74.52.98:3333
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
