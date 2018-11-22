package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 22/11/2018.
 */

public class SideShowModel {
    /**
     * code : 200
     * msg :
     * data : [{"id":5,"sellerId":3,"pic":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/carEB17F088BA63452BA680A78B149908FE.jpg","type":1,"status":0,"createTime":1541401669000}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5
         * sellerId : 3
         * pic : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/carEB17F088BA63452BA680A78B149908FE.jpg
         * type : 1
         * status : 0
         * createTime : 1541401669000
         */

        private int id;
        private int sellerId;
        private String pic;
        private int type;
        private int status;
        private long createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
