package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 版本更新
 * Created by John on 28/2/2018.
 */

public class VersionOutModel {

    /**
     * code : 200
     * msg :
     * data : {"id":1,"appName":"parkingman","version":"1.0.0","rdUrl":"www.xxxxxx.xxxx.com","createTime":1512744552000,"updateTime":0,"ext":"123131"}
     */

    private int code;
    private String msg;
    private VersionModel data;

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

    public VersionModel getData() {
        return data;
    }

    public void setData(VersionModel data) {
        this.data = data;
    }

    public static class VersionModel {
        /**
         * id : 1
         * appName : parkingman
         * version : 1.0.0
         * rdUrl : www.xxxxxx.xxxx.com
         * createTime : 1512744552000
         * updateTime : 0
         * ext : 123131
         */

        private int id;
        private String appName;
        private String version;
        private String rdUrl;
        private long createTime;
        private int updateTime;
        private String ext;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getRdUrl() {
            return rdUrl;
        }

        public void setRdUrl(String rdUrl) {
            this.rdUrl = rdUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }
    }
}
