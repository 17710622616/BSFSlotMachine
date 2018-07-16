package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 5/1/2018.
 */

public class UserOrderOutModel {

    /**
     * code : 0
     * msg :
     * data : {"totalCount":3,"data":[{"id":78,"userId":16,"orderNo":"M201712181610598670001","orderStatus":2,"orderType":2,"machineNo":"","carId":30,"startSlotTime":1513584659000,"totalAmount":900,"discountAmount":90,"payAmount":900,"couponId":"","remark":"季度费:900(300*3)返现90即10%","monthNum":3,"isDelete":0,"createTime":1513584659000,"updateTime":1514386158000,"carType":0,"pillarColor":0,"areaCode":0,"parkingSpace":0,"img1":"","img2":"","img3":"","img4":"","img5":""},{"id":22,"userId":16,"orderNo":"M201710222320082310001","orderStatus":2,"orderType":2,"machineNo":"","carId":35,"startSlotTime":1513584659000,"totalAmount":300,"discountAmount":0,"payAmount":300,"couponId":"","remark":"月费:300","monthNum":1,"isDelete":0,"createTime":1508685608000,"updateTime":1508685881000,"carType":0,"pillarColor":0,"areaCode":0,"parkingSpace":0,"img1":"","img2":"","img3":"","img4":"","img5":""}]}
     */

    private int code;
    private String msg;
    private UserOrderInsideModel data;

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

    public UserOrderInsideModel getData() {
        return data;
    }

    public void setData(UserOrderInsideModel data) {
        this.data = data;
    }

    public static class UserOrderInsideModel {
        /**
         * totalCount : 3
         * data : [{"id":78,"userId":16,"orderNo":"M201712181610598670001","orderStatus":2,"orderType":2,"machineNo":"","carId":30,"startSlotTime":1513584659000,"totalAmount":900,"discountAmount":90,"payAmount":900,"couponId":"","remark":"季度费:900(300*3)返现90即10%","monthNum":3,"isDelete":0,"createTime":1513584659000,"updateTime":1514386158000,"carType":0,"pillarColor":0,"areaCode":0,"parkingSpace":0,"img1":"","img2":"","img3":"","img4":"","img5":""},{"id":22,"userId":16,"orderNo":"M201710222320082310001","orderStatus":2,"orderType":2,"machineNo":"","carId":35,"startSlotTime":1513584659000,"totalAmount":300,"discountAmount":0,"payAmount":300,"couponId":"","remark":"月费:300","monthNum":1,"isDelete":0,"createTime":1508685608000,"updateTime":1508685881000,"carType":0,"pillarColor":0,"areaCode":0,"parkingSpace":0,"img1":"","img2":"","img3":"","img4":"","img5":""}]
         */

        private int totalCount;
        private List<UserOrderModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<UserOrderModel> getData() {
            return data;
        }

        public void setData(List<UserOrderModel> data) {
            this.data = data;
        }

        public static class UserOrderModel {
            /**
             * id : 78
             * userId : 16
             * orderNo : M201712181610598670001
             * orderStatus : 2
             * orderType : 2
             * machineNo :
             * carId : 30
             * startSlotTime : 1513584659000
             * totalAmount : 900
             * discountAmount : 90
             * payAmount : 900
             * couponId :
             * remark : 季度费:900(300*3)返现90即10%
             * monthNum : 3
             * isDelete : 0
             * createTime : 1513584659000
             * updateTime : 1514386158000
             * carType : 0
             * pillarColor : 0
             * areaCode : 0
             * parkingSpace : 0
             * img1 :
             * img2 :
             * img3 :
             * img4 :
             * img5 :
             */

            private int id;
            private int userId;
            private String orderNo;
            private int orderStatus;
            private int orderType;
            private String machineNo;
            private int carId;
            private long startSlotTime;
            private double totalAmount;
            private double discountAmount;
            private double payAmount;
            private String couponId;
            private String remark;
            private int monthNum;
            private int isDelete;
            private long createTime;
            private long updateTime;
            private int carType;
            private String pillarColor;
            private String areaCode;
            private String parkingSpace;
            private String img1;
            private String img2;
            private String img3;
            private String img4;
            private String img5;

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

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getOrderType() {
                return orderType;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public String getMachineNo() {
                return machineNo;
            }

            public void setMachineNo(String machineNo) {
                this.machineNo = machineNo;
            }

            public int getCarId() {
                return carId;
            }

            public void setCarId(int carId) {
                this.carId = carId;
            }

            public long getStartSlotTime() {
                return startSlotTime;
            }

            public void setStartSlotTime(long startSlotTime) {
                this.startSlotTime = startSlotTime;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(double discountAmount) {
                this.discountAmount = discountAmount;
            }

            public double getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(int payAmount) {
                this.payAmount = payAmount;
            }

            public String getCouponId() {
                return couponId;
            }

            public void setCouponId(String couponId) {
                this.couponId = couponId;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getMonthNum() {
                return monthNum;
            }

            public void setMonthNum(int monthNum) {
                this.monthNum = monthNum;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
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

            public int getCarType() {
                return carType;
            }

            public void setCarType(int carType) {
                this.carType = carType;
            }

            public String getPillarColor() {
                return pillarColor;
            }

            public void setPillarColor(String pillarColor) {
                this.pillarColor = pillarColor;
            }

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getParkingSpace() {
                return parkingSpace;
            }

            public void setParkingSpace(String parkingSpace) {
                this.parkingSpace = parkingSpace;
            }

            public String getImg1() {
                return img1;
            }

            public void setImg1(String img1) {
                this.img1 = img1;
            }

            public String getImg2() {
                return img2;
            }

            public void setImg2(String img2) {
                this.img2 = img2;
            }

            public String getImg3() {
                return img3;
            }

            public void setImg3(String img3) {
                this.img3 = img3;
            }

            public String getImg4() {
                return img4;
            }

            public void setImg4(String img4) {
                this.img4 = img4;
            }

            public String getImg5() {
                return img5;
            }

            public void setImg5(String img5) {
                this.img5 = img5;
            }
        }
    }
}
