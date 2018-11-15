package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 15/11/2018.
 */

public class HotSellerOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":11,"data":[{"id":29,"sellerName":"3","sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/carC3C496CEF8A542F6A3953E6249C65E46.jpg","scoreService":"0","productNumber":0,"createTime":1540971191000,"saleMoney":0,"orderCount":4455,"orderCountOver":0,"sellerDes":"3","status":0,"acount":"3","password":"","longitude":113.520135,"latitude":22.173461,"address":"3","phone":"3","sellerType":1,"pageView":0,"meter":"8.42 km"}]}
     */

    private int code;
    private String msg;
    private HotSellerModel data;

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

    public HotSellerModel getData() {
        return data;
    }

    public void setData(HotSellerModel data) {
        this.data = data;
    }

    public static class HotSellerModel {
        /**
         * totalCount : 11
         * data : [{"id":29,"sellerName":"3","sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/carC3C496CEF8A542F6A3953E6249C65E46.jpg","scoreService":"0","productNumber":0,"createTime":1540971191000,"saleMoney":0,"orderCount":4455,"orderCountOver":0,"sellerDes":"3","status":0,"acount":"3","password":"","longitude":113.520135,"latitude":22.173461,"address":"3","phone":"3","sellerType":1,"pageView":0,"meter":"8.42 km"}]
         */

        private int totalCount;
        private List<DataBean> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 29
             * sellerName : 3
             * sellerLogo : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/carC3C496CEF8A542F6A3953E6249C65E46.jpg
             * scoreService : 0
             * productNumber : 0
             * createTime : 1540971191000
             * saleMoney : 0
             * orderCount : 4455
             * orderCountOver : 0
             * sellerDes : 3
             * status : 0
             * acount : 3
             * password :
             * longitude : 113.520135
             * latitude : 22.173461
             * address : 3
             * phone : 3
             * sellerType : 1
             * pageView : 0
             * meter : 8.42 km
             */

            private int id;
            private String sellerName;
            private String sellerLogo;
            private String scoreService;
            private int productNumber;
            private long createTime;
            private int saleMoney;
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
            private int pageView;
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

            public int getSaleMoney() {
                return saleMoney;
            }

            public void setSaleMoney(int saleMoney) {
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

            public int getPageView() {
                return pageView;
            }

            public void setPageView(int pageView) {
                this.pageView = pageView;
            }

            public String getMeter() {
                return meter;
            }

            public void setMeter(String meter) {
                this.meter = meter;
            }
        }
    }
}
