package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John on 13/11/2018.
 */

public class CarWashMerchantOutModel {

    /**
     * code : 200
     * msg :
     * data : {"totalCount":11,"data":[{"id":1,"sellerName":"平台自营","sellerLogo":"","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":10,"orderCount":444,"orderCountOver":1,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.546357,"latitude":22.212178,"address":"珠海","phone":"9999","sellerType":1,"pageView":0,"meter":"1.43 km"},{"id":3,"sellerName":"蜜麻花","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1458649198000,"saleMoney":0,"orderCount":654,"orderCountOver":0,"sellerDes":"蜜麻花","status":0,"acount":"admin10","password":null,"longitude":113.543353,"latitude":22.210231,"address":"澳门","phone":"666666","sellerType":1,"pageView":0,"meter":"1.62 km"},{"id":4,"sellerName":"济南学院","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1458701287000,"saleMoney":0,"orderCount":88,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin9","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":5,"sellerName":"惠生活自营","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1460451801000,"saleMoney":0,"orderCount":444,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin8","password":null,"longitude":113.548116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"1.04 km"},{"id":6,"sellerName":"111111","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1462761928000,"saleMoney":0,"orderCount":66,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin7","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":7,"sellerName":"wangpeng000","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1470651429000,"saleMoney":0,"orderCount":755,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin6","password":null,"longitude":113.558116,"latitude":22.215384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"1.12 km"},{"id":8,"sellerName":"312312","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1475896189000,"saleMoney":0,"orderCount":455,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin5","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":9,"sellerName":"4535","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1475897782000,"saleMoney":0,"orderCount":555,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin4","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":10,"sellerName":"12312","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1476415911000,"saleMoney":0,"orderCount":4,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin3","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":11,"sellerName":"测试有限公司","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1478136096000,"saleMoney":0,"orderCount":444,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin2","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"}]}
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
         * totalCount : 11
         * data : [{"id":1,"sellerName":"平台自营","sellerLogo":"","scoreService":"4.0","productNumber":21,"createTime":1456910927000,"saleMoney":10,"orderCount":444,"orderCountOver":1,"sellerDes":"自营","status":0,"acount":"admin","password":"","longitude":113.546357,"latitude":22.212178,"address":"珠海","phone":"9999","sellerType":1,"pageView":0,"meter":"1.43 km"},{"id":3,"sellerName":"蜜麻花","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1458649198000,"saleMoney":0,"orderCount":654,"orderCountOver":0,"sellerDes":"蜜麻花","status":0,"acount":"admin10","password":null,"longitude":113.543353,"latitude":22.210231,"address":"澳门","phone":"666666","sellerType":1,"pageView":0,"meter":"1.62 km"},{"id":4,"sellerName":"济南学院","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1458701287000,"saleMoney":0,"orderCount":88,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin9","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":5,"sellerName":"惠生活自营","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1460451801000,"saleMoney":0,"orderCount":444,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin8","password":null,"longitude":113.548116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"1.04 km"},{"id":6,"sellerName":"111111","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1462761928000,"saleMoney":0,"orderCount":66,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin7","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":7,"sellerName":"wangpeng000","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1470651429000,"saleMoney":0,"orderCount":755,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin6","password":null,"longitude":113.558116,"latitude":22.215384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"1.12 km"},{"id":8,"sellerName":"312312","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1475896189000,"saleMoney":0,"orderCount":455,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin5","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":9,"sellerName":"4535","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1475897782000,"saleMoney":0,"orderCount":555,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin4","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":10,"sellerName":"12312","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1476415911000,"saleMoney":0,"orderCount":4,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin3","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"},{"id":11,"sellerName":"测试有限公司","sellerLogo":null,"scoreService":"0","productNumber":0,"createTime":1478136096000,"saleMoney":0,"orderCount":444,"orderCountOver":0,"sellerDes":"","status":0,"acount":"admin2","password":null,"longitude":113.558116,"latitude":22.205384,"address":null,"phone":null,"sellerType":1,"pageView":0,"meter":"0m"}]
         */

        private int totalCount;
        private List<CarWashMerchatModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
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
             * saleMoney : 10
             * orderCount : 444
             * orderCountOver : 1
             * sellerDes : 自营
             * status : 0
             * acount : admin
             * password :
             * longitude : 113.546357
             * latitude : 22.212178
             * address : 珠海
             * phone : 9999
             * sellerType : 1
             * pageView : 0
             * meter : 1.43 km
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
