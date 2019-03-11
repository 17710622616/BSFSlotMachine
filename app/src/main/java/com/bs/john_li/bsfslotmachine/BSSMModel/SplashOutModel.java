package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John_Li on 8/3/2019.
 */

public class SplashOutModel {
    /**
     * code : 200
     * msg :
     * data : {"id":1,"pic":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car6817CA5C4C944189BF5DE88D5806B751.png","type":0,"createTime":"2019-03-06 12:10:23.0"}
     */

    private int code;
    private String msg;
    private SplashModel data;

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

    public SplashModel getData() {
        return data;
    }

    public void setData(SplashModel data) {
        this.data = data;
    }

    public static class SplashModel {
        /**
         * id : 1
         * pic : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car6817CA5C4C944189BF5DE88D5806B751.png
         * type : 0
         * createTime : 2019-03-06 12:10:23.0
         */

        private int id;
        private String pic;
        private int type;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
