package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 14/11/2018.
 */

public class MerchatSetOutModel {
    /**
     * code : 200
     * msg :
     * data : {"seller":{"id":1,"sellerName":"平台自营","sellerLogo":"","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":172951.24,"orderCount":181,"orderCountOver":55,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.559116,"latitude":22.205484,"address":"珠海","phone":"9999","sellerType":1,"meter":""},"sellerCharge":[{"id":4,"sellerId":1,"chargeName":"套餐A","marketPrice":50,"costPrice":10,"description":"无","createTime":1541493987000,"updateTime":""},{"id":5,"sellerId":1,"chargeName":"套餐B","marketPrice":60,"costPrice":9,"description":"无","createTime":1541494000000,"updateTime":""}]}
     */

    private int code;
    private String msg;
    private MerchatSetModel data;

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

    public MerchatSetModel getData() {
        return data;
    }

    public void setData(MerchatSetModel data) {
        this.data = data;
    }

    public static class MerchatSetModel {
        /**
         * seller : {"id":1,"sellerName":"平台自营","sellerLogo":"","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":172951.24,"orderCount":181,"orderCountOver":55,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.559116,"latitude":22.205484,"address":"珠海","phone":"9999","sellerType":1,"meter":""}
         * sellerCharge : [{"id":4,"sellerId":1,"chargeName":"套餐A","marketPrice":50,"costPrice":10,"description":"无","createTime":1541493987000,"updateTime":""},{"id":5,"sellerId":1,"chargeName":"套餐B","marketPrice":60,"costPrice":9,"description":"无","createTime":1541494000000,"updateTime":""}]
         */

        private SellerBean seller;
        private List<SellerChargeBean> sellerCharge;

        public SellerBean getSeller() {
            return seller;
        }

        public void setSeller(SellerBean seller) {
            this.seller = seller;
        }

        public List<SellerChargeBean> getSellerCharge() {
            return sellerCharge;
        }

        public void setSellerCharge(List<SellerChargeBean> sellerCharge) {
            this.sellerCharge = sellerCharge;
        }

        public static class SellerBean {
            /**
             * id : 1
             * sellerName : 平台自营
             * sellerLogo :
             * scoreService : 4.0
             * productNumber : 21
             * createTime : 1456910927000
             * saleMoney : 172951.24
             * orderCount : 181
             * orderCountOver : 55
             * sellerDes : 自营
             * status : 0
             * acount : admin
             * password :
             * longitude : 113.559116
             * latitude : 22.205484
             * address : 珠海
             * phone : 9999
             * sellerType : 1
             * meter :
             */

            private int id;
            private String sellerName;
            private String sellerLogo;
            private String scoreService;
            private int productNumber;
            private long createTime;
            private double saleMoney;
            private int orderCount;
            private int orderCountOver;
            private String sellerDes;
            private int status;
            private String acount;
            private String password;
            private double longitude;
            private double latitude;
            private String address;
            private String phone;
            private int sellerType;
            private String meter;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }

            public String getSellerLogo() {
                return sellerLogo;
            }

            public void setSellerLogo(String sellerLogo) {
                this.sellerLogo = sellerLogo;
            }

            public String getScoreService() {
                return scoreService;
            }

            public void setScoreService(String scoreService) {
                this.scoreService = scoreService;
            }

            public int getProductNumber() {
                return productNumber;
            }

            public void setProductNumber(int productNumber) {
                this.productNumber = productNumber;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public double getSaleMoney() {
                return saleMoney;
            }

            public void setSaleMoney(double saleMoney) {
                this.saleMoney = saleMoney;
            }

            public int getOrderCount() {
                return orderCount;
            }

            public void setOrderCount(int orderCount) {
                this.orderCount = orderCount;
            }

            public int getOrderCountOver() {
                return orderCountOver;
            }

            public void setOrderCountOver(int orderCountOver) {
                this.orderCountOver = orderCountOver;
            }

            public String getSellerDes() {
                return sellerDes;
            }

            public void setSellerDes(String sellerDes) {
                this.sellerDes = sellerDes;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAcount() {
                return acount;
            }

            public void setAcount(String acount) {
                this.acount = acount;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getSellerType() {
                return sellerType;
            }

            public void setSellerType(int sellerType) {
                this.sellerType = sellerType;
            }

            public String getMeter() {
                return meter;
            }

            public void setMeter(String meter) {
                this.meter = meter;
            }
        }

        public static class SellerChargeBean {
            /**
             * id : 4
             * sellerId : 1
             * chargeName : 套餐A
             * marketPrice : 50.0
             * costPrice : 10.0
             * description : 无
             * createTime : 1541493987000
             * updateTime :
             */

            private int id;
            private int sellerId;
            private String chargeName;
            private double marketPrice;
            private double costPrice;
            private String description;
            private long createTime;
            private String updateTime;

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

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
