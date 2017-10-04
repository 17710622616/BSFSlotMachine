package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 4/10/2017.
 */

public class TestCarListModel {

    /**
     * code : 200
     * msg :
     * data : [{"id":12,"userId":16,"imgUrl":"objectNam1","ifPerson":0,"carNo":"浙A88888","modelForCar":"SUV","carBrand":"法拉利","carStyle":"","ifPay":1,"isDelete":"","createTime":"","updateTime":""}]
     */

    private int code;
    private String msg;
    private List<CarModel> data;

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

    public List<CarModel> getData() {
        return data;
    }

    public void setData(List<CarModel> data) {
        this.data = data;
    }

    public static class CarModel {
        /**
         * id : 12
         * userId : 16
         * imgUrl : objectNam1
         * ifPerson : 0
         * carNo : 浙A88888
         * modelForCar : SUV
         * carBrand : 法拉利
         * carStyle :
         * ifPay : 1
         * isDelete :
         * createTime :
         * updateTime :
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
        private String isDelete;
        private String createTime;
        private String updateTime;

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

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
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
