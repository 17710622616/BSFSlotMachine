package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 4/1/2019.
 */

public class CarPartsOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":1,"data":[{"id":1,"sellerId":1,"name":"超级耐磨轮胎","marketPrice":2100,"costPrice":888,"description":"超级耐磨轮胎，特能输牌子，摩擦摩擦爽翻天","createTime":1543829921000,"updateTime":1546395295000,"ifDelete":0,"status":2,"pics":"http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg,https://parkman-web.oss-cn-shenzhen.aliyuncs.com/luntai.jpg","headPic":"http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg"}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * totalCount : 1
         * data : [{"id":1,"sellerId":1,"name":"超级耐磨轮胎","marketPrice":2100,"costPrice":888,"description":"超级耐磨轮胎，特能输牌子，摩擦摩擦爽翻天","createTime":1543829921000,"updateTime":1546395295000,"ifDelete":0,"status":2,"pics":"http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg,https://parkman-web.oss-cn-shenzhen.aliyuncs.com/luntai.jpg","headPic":"http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg"}]
         */

        private int totalCount;
        private List<CarPatsModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<CarPatsModel> getData() {
            return data;
        }

        public void setData(List<CarPatsModel> data) {
            this.data = data;
        }

        public static class CarPatsModel {
            /**
             * id : 1
             * sellerId : 1
             * name : 超级耐磨轮胎
             * marketPrice : 2100
             * costPrice : 888
             * description : 超级耐磨轮胎，特能输牌子，摩擦摩擦爽翻天
             * createTime : 1543829921000
             * updateTime : 1546395295000
             * ifDelete : 0
             * status : 2
             * pics : http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg,https://parkman-web.oss-cn-shenzhen.aliyuncs.com/luntai.jpg
             * headPic : http://image.sonhoo.com/server14/photos/photo/lisaglobal/f98e0dea756431bdd29f39ae06fe0571.jpg
             */

            private int id;
            private int sellerId;
            private String name;
            private int marketPrice;
            private int costPrice;
            private String description;
            private long createTime;
            private long updateTime;
            private int ifDelete;
            private int status;
            private String pics;
            private String headPic;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(int marketPrice) {
                this.marketPrice = marketPrice;
            }

            public int getCostPrice() {
                return costPrice;
            }

            public void setCostPrice(int costPrice) {
                this.costPrice = costPrice;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public int getIfDelete() {
                return ifDelete;
            }

            public void setIfDelete(int ifDelete) {
                this.ifDelete = ifDelete;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getPics() {
                return pics;
            }

            public void setPics(String pics) {
                this.pics = pics;
            }

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }
        }
    }
}
