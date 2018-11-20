package com.cocacola.john_li.bsfmerchantsversionapp.Model;

import java.util.List;

/**
 * Created by John_Li on 20/11/2018.
 */

public class SellerSetOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":3,"data":[{"id":12,"sellerId":1,"chargeName":"套餐C","marketPrice":50,"costPrice":50,"description":"无","createTime":1542265792000,"updateTime":1542265820000,"exprieTime":1543075200000,"ifDelete":0,"status":"2 //0创建  1提交审核  2审核通过  3审核失败"},{"id":11,"sellerId":1,"chargeName":"套餐B","marketPrice":50,"costPrice":0,"description":"无","createTime":1542265770000,"updateTime":1542265882000,"exprieTime":1543129780000,"ifDelete":0,"status":2},{"id":4,"sellerId":1,"chargeName":"套餐A","marketPrice":50,"costPrice":10,"description":"无","createTime":1542005940000,"updateTime":1542265894000,"exprieTime":1543420800000,"ifDelete":0,"status":2}]}
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
         * totalCount : 3
         * data : [{"id":12,"sellerId":1,"chargeName":"套餐C","marketPrice":50,"costPrice":50,"description":"无","createTime":1542265792000,"updateTime":1542265820000,"exprieTime":1543075200000,"ifDelete":0,"status":"2 //0创建  1提交审核  2审核通过  3审核失败"},{"id":11,"sellerId":1,"chargeName":"套餐B","marketPrice":50,"costPrice":0,"description":"无","createTime":1542265770000,"updateTime":1542265882000,"exprieTime":1543129780000,"ifDelete":0,"status":2},{"id":4,"sellerId":1,"chargeName":"套餐A","marketPrice":50,"costPrice":10,"description":"无","createTime":1542005940000,"updateTime":1542265894000,"exprieTime":1543420800000,"ifDelete":0,"status":2}]
         */

        private int totalCount;
        private List<SellerSetModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<SellerSetModel> getData() {
            return data;
        }

        public void setData(List<SellerSetModel> data) {
            this.data = data;
        }

        public static class SellerSetModel {
            /**
             * id : 12
             * sellerId : 1
             * chargeName : 套餐C
             * marketPrice : 50.0
             * costPrice : 50.0
             * description : 无
             * createTime : 1542265792000
             * updateTime : 1542265820000
             * exprieTime : 1543075200000
             * ifDelete : 0
             * status : 2 //0创建  1提交审核  2审核通过  3审核失败
             */

            private int id;
            private int sellerId;
            private String chargeName;
            private double marketPrice;
            private double costPrice;
            private String description;
            private long createTime;
            private long updateTime;
            private long exprieTime;
            private int ifDelete;
            private int status;

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

            public String getChargeName() {
                return chargeName;
            }

            public void setChargeName(String chargeName) {
                this.chargeName = chargeName;
            }

            public double getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(double marketPrice) {
                this.marketPrice = marketPrice;
            }

            public double getCostPrice() {
                return costPrice;
            }

            public void setCostPrice(double costPrice) {
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

            public long getExprieTime() {
                return exprieTime;
            }

            public void setExprieTime(long exprieTime) {
                this.exprieTime = exprieTime;
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
        }
    }
}
