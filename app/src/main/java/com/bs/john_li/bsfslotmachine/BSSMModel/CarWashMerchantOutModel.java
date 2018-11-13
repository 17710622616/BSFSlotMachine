package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 13/11/2018.
 */

public class CarWashMerchantOutModel {
    /**
     * code : 200
     * msg :
     * data : [{"id":1,"sellerName":"平台自营","sellerLogo":"","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":172951.24,"orderCount":181,"orderCountOver":55,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.559116,"latitude":22.205484,"address":"珠海","phone":"9999","sellerType":1,"meter":"104m"},{"id":5,"sellerName":"惠生活自营","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1460451801000,"saleMoney":0,"orderCount":2,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin8","password":null,"longitude":113.548116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"meter":"1.04 km"}]
     */

    private int code;
    private String msg;
    private List<CarWashMerchatModel> data;

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

    public List<CarWashMerchatModel> getData() {
        return data;
    }

    public void setData(List<CarWashMerchatModel> data) {
        this.data = data;
    }

    public static class CarWashMerchatModel {
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
         * meter : 104m
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
}
