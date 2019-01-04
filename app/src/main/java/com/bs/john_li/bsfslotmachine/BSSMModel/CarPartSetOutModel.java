package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 4/1/2019.
 */

public class CarPartSetOutModel {
    /**
     * code : 200
     * msg :
     * data : {"sellerParts":{"id":1,"sellerId":1,"name":"超级耐磨轮胎","marketPrice":2100,"costPrice":888,"description":"超级耐磨轮胎，特能输牌子，摩擦摩擦爽翻天","createTime":1543829921000,"updateTime":1546395295000,"ifDelete":0,"status":2,"pics":"","headPic":""},"sellerCarList":[{"id":1,"carNo":"MC-96382","carType":0,"carBrand":"Bentley","carSeries":"慕尚","carStyle":null,"firstRegisterationTime":1546395046000,"driverMileage":5,"carGears":1,"carImg":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,","carPrices":540,"tel":"62333982","isTelDisplayed":0,"carDescription":"全新","carColor":"香檳色","type":"私家車,貨車","exhaust":"6.8","countryOfOrigin":"","pageView":32,"deliveryTime":1546395046000,"releaseDate":1546395046000,"periodValidity":1546395046000,"configInfo":"","stateOfRepiar":"","insideBody":"","testConclusion":"","sellerId":1,"userId":1,"ifDelete":0,"ifCollection":0,"status":2,"createTime":1543834231000,"updateTime":1546395046000}],"sellerChargeList":[{"id":1,"sellerId":1,"chargeName":"特價單次洗車(洗車,吸塵,水蠟)","marketPrice":200,"costPrice":1,"description":"大特價，洗車+吸塵+水蠟=1蚊，平台自營優惠更多！","createTime":1543829921000,"updateTime":1543829944000,"exprieTime":1577721600000,"ifDelete":0,"status":2}]}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sellerParts : {"id":1,"sellerId":1,"name":"超级耐磨轮胎","marketPrice":2100,"costPrice":888,"description":"超级耐磨轮胎，特能输牌子，摩擦摩擦爽翻天","createTime":1543829921000,"updateTime":1546395295000,"ifDelete":0,"status":2,"pics":"","headPic":""}
         * sellerCarList : [{"id":1,"carNo":"MC-96382","carType":0,"carBrand":"Bentley","carSeries":"慕尚","carStyle":null,"firstRegisterationTime":1546395046000,"driverMileage":5,"carGears":1,"carImg":"https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,","carPrices":540,"tel":"62333982","isTelDisplayed":0,"carDescription":"全新","carColor":"香檳色","type":"私家車,貨車","exhaust":"6.8","countryOfOrigin":"","pageView":32,"deliveryTime":1546395046000,"releaseDate":1546395046000,"periodValidity":1546395046000,"configInfo":"","stateOfRepiar":"","insideBody":"","testConclusion":"","sellerId":1,"userId":1,"ifDelete":0,"ifCollection":0,"status":2,"createTime":1543834231000,"updateTime":1546395046000}]
         * sellerChargeList : [{"id":1,"sellerId":1,"chargeName":"特價單次洗車(洗車,吸塵,水蠟)","marketPrice":200,"costPrice":1,"description":"大特價，洗車+吸塵+水蠟=1蚊，平台自營優惠更多！","createTime":1543829921000,"updateTime":1543829944000,"exprieTime":1577721600000,"ifDelete":0,"status":2}]
         */

        private SellerPartsBean sellerParts;
        private List<SellerCarListBean> sellerCarList;
        private List<SellerChargeListBean> sellerChargeList;

        public SellerPartsBean getSellerParts() {
            return sellerParts;
        }

        public void setSellerParts(SellerPartsBean sellerParts) {
            this.sellerParts = sellerParts;
        }

        public List<SellerCarListBean> getSellerCarList() {
            return sellerCarList;
        }

        public void setSellerCarList(List<SellerCarListBean> sellerCarList) {
            this.sellerCarList = sellerCarList;
        }

        public List<SellerChargeListBean> getSellerChargeList() {
            return sellerChargeList;
        }

        public void setSellerChargeList(List<SellerChargeListBean> sellerChargeList) {
            this.sellerChargeList = sellerChargeList;
        }

        public static class SellerPartsBean {
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
             * pics :
             * headPic :
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

        public static class SellerCarListBean {
            /**
             * id : 1
             * carNo : MC-96382
             * carType : 0
             * carBrand : Bentley
             * carSeries : 慕尚
             * carStyle : null
             * firstRegisterationTime : 1546395046000
             * driverMileage : 5
             * carGears : 1
             * carImg : https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,https://parkingman-pic.oss-cn-shenzhen.aliyuncs.com/car371BF334BA3D4CACA0674CDAEA743A60.png,
             * carPrices : 540
             * tel : 62333982
             * isTelDisplayed : 0
             * carDescription : 全新
             * carColor : 香檳色
             * type : 私家車,貨車
             * exhaust : 6.8
             * countryOfOrigin :
             * pageView : 32
             * deliveryTime : 1546395046000
             * releaseDate : 1546395046000
             * periodValidity : 1546395046000
             * configInfo :
             * stateOfRepiar :
             * insideBody :
             * testConclusion :
             * sellerId : 1
             * userId : 1
             * ifDelete : 0
             * ifCollection : 0
             * status : 2
             * createTime : 1543834231000
             * updateTime : 1546395046000
             */

            private int id;
            private String carNo;
            private int carType;
            private String carBrand;
            private String carSeries;
            private Object carStyle;
            private long firstRegisterationTime;
            private int driverMileage;
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
            private int ifCollection;
            private int status;
            private long createTime;
            private long updateTime;

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

            public Object getCarStyle() {
                return carStyle;
            }

            public void setCarStyle(Object carStyle) {
                this.carStyle = carStyle;
            }

            public long getFirstRegisterationTime() {
                return firstRegisterationTime;
            }

            public void setFirstRegisterationTime(long firstRegisterationTime) {
                this.firstRegisterationTime = firstRegisterationTime;
            }

            public int getDriverMileage() {
                return driverMileage;
            }

            public void setDriverMileage(int driverMileage) {
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

            public int getIfCollection() {
                return ifCollection;
            }

            public void setIfCollection(int ifCollection) {
                this.ifCollection = ifCollection;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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

        public static class SellerChargeListBean {
            /**
             * id : 1
             * sellerId : 1
             * chargeName : 特價單次洗車(洗車,吸塵,水蠟)
             * marketPrice : 200
             * costPrice : 1
             * description : 大特價，洗車+吸塵+水蠟=1蚊，平台自營優惠更多！
             * createTime : 1543829921000
             * updateTime : 1543829944000
             * exprieTime : 1577721600000
             * ifDelete : 0
             * status : 2
             */

            private int id;
            private int sellerId;
            private String chargeName;
            private int marketPrice;
            private int costPrice;
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
