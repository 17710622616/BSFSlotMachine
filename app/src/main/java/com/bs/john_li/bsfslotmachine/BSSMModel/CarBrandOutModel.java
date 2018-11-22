package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 23/11/2018.
 */

public class CarBrandOutModel {
    /**
     * code : 200
     * msg : 获取列表成功
     * data : [{"id":17,"name":"亚琛施纳泽","engName":"AC Schnitzer","imgPath":"http://car1.autoimg.cn/logo/brand/50/129302871545000000.jpg","autoHomePath":"http://car.autohome.com.cn/price/brand-117.html","createTime":1541645707000,"updateTime":1432699574000}]
     */

    private int code;
    private String msg;
    private List<CarBrandModel> data;

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

    public List<CarBrandModel> getData() {
        return data;
    }

    public void setData(List<CarBrandModel> data) {
        this.data = data;
    }

    public static class CarBrandModel {
        /**
         * id : 17
         * name : 亚琛施纳泽
         * engName : AC Schnitzer
         * imgPath : http://car1.autoimg.cn/logo/brand/50/129302871545000000.jpg
         * autoHomePath : http://car.autohome.com.cn/price/brand-117.html
         * createTime : 1541645707000
         * updateTime : 1432699574000
         */

        private int id;
        private String name;
        private String engName;
        private String imgPath;
        private String autoHomePath;
        private long createTime;
        private long updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEngName() {
            return engName;
        }

        public void setEngName(String engName) {
            this.engName = engName;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getAutoHomePath() {
            return autoHomePath;
        }

        public void setAutoHomePath(String autoHomePath) {
            this.autoHomePath = autoHomePath;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
