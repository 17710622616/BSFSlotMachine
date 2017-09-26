package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * 车辆类
 * Created by John on 26/9/2017.
 */

public class CarModel {

    /**
     * code : 200
     * msg : null
     * data : {"totalCount":2,"data":[{"id":9,"userId":996,"imgUrl":"objectNam1","ifPerson":0,"carNo":"浙A66655","modelForCar":"SUV","carBrand":"法拉利","carStyle":"幻影","ifPay":0,"isDelete":null,"createTime":null,"updateTime":null},{"id":10,"userId":996,"imgUrl":"objectNam1","ifPerson":0,"carNo":"123","modelForCar":"MPV","carBrand":"223","carStyle":"zghu","ifPay":0,"isDelete":null,"createTime":null,"updateTime":null}]}
     */

    private int code;
    private Object msg;
    private CarCountAndListModel data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public CarCountAndListModel getData() {
        return data;
    }

    public void setData(CarCountAndListModel data) {
        this.data = data;
    }

    public static class CarCountAndListModel {
        /**
         * totalCount : 2
         * data : [{"id":9,"userId":996,"imgUrl":"objectNam1","ifPerson":0,"carNo":"浙A66655","modelForCar":"SUV","carBrand":"法拉利","carStyle":"幻影","ifPay":0,"isDelete":null,"createTime":null,"updateTime":null},{"id":10,"userId":996,"imgUrl":"objectNam1","ifPerson":0,"carNo":"123","modelForCar":"MPV","carBrand":"223","carStyle":"zghu","ifPay":0,"isDelete":null,"createTime":null,"updateTime":null}]
         */

        private int totalCount;
        private List<CarInsideModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<CarInsideModel> getData() {
            return data;
        }

        public void setData(List<CarInsideModel> data) {
            this.data = data;
        }

        public static class CarInsideModel {
            /**
             * id : 9
             * userId : 996
             * imgUrl : objectNam1
             * ifPerson : 0
             * carNo : 浙A66655
             * modelForCar : SUV
             * carBrand : 法拉利
             * carStyle : 幻影
             * ifPay : 0
             * isDelete : null
             * createTime : null
             * updateTime : null
             */

            private int id;
            private int userId;
            private String imgUrl;
            private int ifPerson;
            private String carNo;
            private String modelForCar;
            private String carBrand;
            private String carStyle;
            private int ifPay;
            private Object isDelete;
            private Object createTime;
            private Object updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getIfPerson() {
                return ifPerson;
            }

            public void setIfPerson(int ifPerson) {
                this.ifPerson = ifPerson;
            }

            public String getCarNo() {
                return carNo;
            }

            public void setCarNo(String carNo) {
                this.carNo = carNo;
            }

            public String getModelForCar() {
                return modelForCar;
            }

            public void setModelForCar(String modelForCar) {
                this.modelForCar = modelForCar;
            }

            public String getCarBrand() {
                return carBrand;
            }

            public void setCarBrand(String carBrand) {
                this.carBrand = carBrand;
            }

            public String getCarStyle() {
                return carStyle;
            }

            public void setCarStyle(String carStyle) {
                this.carStyle = carStyle;
            }

            public int getIfPay() {
                return ifPay;
            }

            public void setIfPay(int ifPay) {
                this.ifPay = ifPay;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
