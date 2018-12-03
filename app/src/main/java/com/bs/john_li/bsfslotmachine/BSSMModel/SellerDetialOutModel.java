package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John on 28/11/2018.
 */

public class SellerDetialOutModel {
    /**
     * code : 200
     * msg :
     * data : {"id":11,"sellerName":"澳门二手车网","sellerLogo":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car9D09DC6DAFA648B0AA6CE265DC2B8365.png","scoreService":"0","productNumber":0,"createTime":1478136096000,"saleMoney":0,"orderCount":448,"orderCountOver":0,"sellerDes":"二手车网","status":0,"acount":"admin2","password":"","longitude":100,"latitude":22.205384,"address":"花地瑪堂區","phone":"6666666","sellerType":2,"pageView":0,"businessHours":"9:00-16:00","meter":""}
     */

    private int code;
    private String msg;
    private SellerDetialModel data;

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

    public SellerDetialModel getData() {
        return data;
    }

    public void setData(SellerDetialModel data) {
        this.data = data;
    }

    public static class SellerDetialModel {
        /**
         * id : 11
         * sellerName : 澳门二手车网
         * sellerLogo : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car9D09DC6DAFA648B0AA6CE265DC2B8365.png
         * scoreService : 0
         * productNumber : 0
         * createTime : 1478136096000
         * saleMoney : 0
         * orderCount : 448
         * orderCountOver : 0
         * sellerDes : 二手车网
         * status : 0
         * acount : admin2
         * password :
         * longitude : 100
         * latitude : 22.205384
         * address : 花地瑪堂區
         * phone : 6666666
         * sellerType : 2
         * pageView : 0
         * businessHours : 9:00-16:00
         * meter :
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
        private int longitude;
        private double latitude;
        private String address;
        private String phone;
        private int sellerType;
        private int pageView;
        private String businessHours;
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

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
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

        public String getBusinessHours() {
            return businessHours;
        }

        public void setBusinessHours(String businessHours) {
            this.businessHours = businessHours;
        }

        public String getMeter() {
            return meter;
        }

        public void setMeter(String meter) {
            this.meter = meter;
        }
    }
}
