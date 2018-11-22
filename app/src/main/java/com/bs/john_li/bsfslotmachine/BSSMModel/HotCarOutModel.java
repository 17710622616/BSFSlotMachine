package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 22/11/2018.
 */

public class HotCarOutModel {
    /**
     * code : 200
     * msg :
     * data : {"totalCount":6,"data":[{"id":6,"carNo":"MH1232","carType":0,"carBrand":"BMW","carSeries":"S2199","carStyle":"w1333","firstRegisterationTime":1541747169000,"driverMileage":3.8,"carGears":1,"carImg":"http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg,http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg","carPrices":590000,"tel":"254444114","isTelDisplayed":0,"carDescription":"撤非常的好，保养也很到位哦","carColor":"红色","type":"客貨車,跑車","exhaust":"1.7","countryOfOrigin":"德国","pageView":0,"deliveryTime":1541747169000,"releaseDate":1541747169000,"periodValidity":1541747169000,"configInfo":"xx","stateOfRepiar":"xx","insideBody":"xx","testConclusion":"xx","sellerId":2,"userId":123456789,"ifDelete":0,"status":2}]}
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
         * totalCount : 6
         * data : [{"id":6,"carNo":"MH1232","carType":0,"carBrand":"BMW","carSeries":"S2199","carStyle":"w1333","firstRegisterationTime":1541747169000,"driverMileage":3.8,"carGears":1,"carImg":"http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg,http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg","carPrices":590000,"tel":"254444114","isTelDisplayed":0,"carDescription":"撤非常的好，保养也很到位哦","carColor":"红色","type":"客貨車,跑車","exhaust":"1.7","countryOfOrigin":"德国","pageView":0,"deliveryTime":1541747169000,"releaseDate":1541747169000,"periodValidity":1541747169000,"configInfo":"xx","stateOfRepiar":"xx","insideBody":"xx","testConclusion":"xx","sellerId":2,"userId":123456789,"ifDelete":0,"status":2}]
         */

        private int totalCount;
        private List<HotCarModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<HotCarModel> getData() {
            return data;
        }

        public void setData(List<HotCarModel> data) {
            this.data = data;
        }

        public static class HotCarModel {
            /**
             * id : 6
             * carNo : MH1232
             * carType : 0
             * carBrand : BMW
             * carSeries : S2199
             * carStyle : w1333
             * firstRegisterationTime : 1541747169000
             * driverMileage : 3.8
             * carGears : 1
             * carImg : http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg,http://test-pic-666.oss-cn-hongkong.aliyuncs.com/car20180309163702.jpg
             * carPrices : 590000
             * tel : 254444114
             * isTelDisplayed : 0
             * carDescription : 撤非常的好，保养也很到位哦
             * carColor : 红色
             * type : 客貨車,跑車
             * exhaust : 1.7
             * countryOfOrigin : 德国
             * pageView : 0
             * deliveryTime : 1541747169000
             * releaseDate : 1541747169000
             * periodValidity : 1541747169000
             * configInfo : xx
             * stateOfRepiar : xx
             * insideBody : xx
             * testConclusion : xx
             * sellerId : 2
             * userId : 123456789
             * ifDelete : 0
             * status : 2
             */

            private int id;
            private String carNo;
            private int carType;
            private String carBrand;
            private String carSeries;
            private String carStyle;
            private long firstRegisterationTime;
            private double driverMileage;
            private int carGears;
            private String carImg;
            private int carPrices;
            private String tel;
            private int isTelDisplayed;
            private String carDescription;
            private String carColor;
            private String type;
            private String exhaust;
            private String countryOfOrigin;
            private int pageView;
            private long deliveryTime;
            private long releaseDate;
            private long periodValidity;
            private String configInfo;
            private String stateOfRepiar;
            private String insideBody;
            private String testConclusion;
            private int sellerId;
            private int userId;
            private int ifDelete;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCarNo() {
                return carNo;
            }

            public void setCarNo(String carNo) {
                this.carNo = carNo;
            }

            public int getCarType() {
                return carType;
            }

            public void setCarType(int carType) {
                this.carType = carType;
            }

            public String getCarBrand() {
                return carBrand;
            }

            public void setCarBrand(String carBrand) {
                this.carBrand = carBrand;
            }

            public String getCarSeries() {
                return carSeries;
            }

            public void setCarSeries(String carSeries) {
                this.carSeries = carSeries;
            }

            public String getCarStyle() {
                return carStyle;
            }

            public void setCarStyle(String carStyle) {
                this.carStyle = carStyle;
            }

            public long getFirstRegisterationTime() {
                return firstRegisterationTime;
            }

            public void setFirstRegisterationTime(long firstRegisterationTime) {
                this.firstRegisterationTime = firstRegisterationTime;
            }

            public double getDriverMileage() {
                return driverMileage;
            }

            public void setDriverMileage(double driverMileage) {
                this.driverMileage = driverMileage;
            }

            public int getCarGears() {
                return carGears;
            }

            public void setCarGears(int carGears) {
                this.carGears = carGears;
            }

            public String getCarImg() {
                return carImg;
            }

            public void setCarImg(String carImg) {
                this.carImg = carImg;
            }

            public int getCarPrices() {
                return carPrices;
            }

            public void setCarPrices(int carPrices) {
                this.carPrices = carPrices;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public int getIsTelDisplayed() {
                return isTelDisplayed;
            }

            public void setIsTelDisplayed(int isTelDisplayed) {
                this.isTelDisplayed = isTelDisplayed;
            }

            public String getCarDescription() {
                return carDescription;
            }

            public void setCarDescription(String carDescription) {
                this.carDescription = carDescription;
            }

            public String getCarColor() {
                return carColor;
            }

            public void setCarColor(String carColor) {
                this.carColor = carColor;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getExhaust() {
                return exhaust;
            }

            public void setExhaust(String exhaust) {
                this.exhaust = exhaust;
            }

            public String getCountryOfOrigin() {
                return countryOfOrigin;
            }

            public void setCountryOfOrigin(String countryOfOrigin) {
                this.countryOfOrigin = countryOfOrigin;
            }

            public int getPageView() {
                return pageView;
            }

            public void setPageView(int pageView) {
                this.pageView = pageView;
            }

            public long getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(long deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            public long getReleaseDate() {
                return releaseDate;
            }

            public void setReleaseDate(long releaseDate) {
                this.releaseDate = releaseDate;
            }

            public long getPeriodValidity() {
                return periodValidity;
            }

            public void setPeriodValidity(long periodValidity) {
                this.periodValidity = periodValidity;
            }

            public String getConfigInfo() {
                return configInfo;
            }

            public void setConfigInfo(String configInfo) {
                this.configInfo = configInfo;
            }

            public String getStateOfRepiar() {
                return stateOfRepiar;
            }

            public void setStateOfRepiar(String stateOfRepiar) {
                this.stateOfRepiar = stateOfRepiar;
            }

            public String getInsideBody() {
                return insideBody;
            }

            public void setInsideBody(String insideBody) {
                this.insideBody = insideBody;
            }

            public String getTestConclusion() {
                return testConclusion;
            }

            public void setTestConclusion(String testConclusion) {
                this.testConclusion = testConclusion;
            }

            public int getSellerId() {
                return sellerId;
            }

            public void setSellerId(int sellerId) {
                this.sellerId = sellerId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
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
