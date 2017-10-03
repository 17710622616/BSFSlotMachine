package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 4/10/2017.
 */

public class TestSlotMachineListModel {

    /**
     * code : 200
     * msg :
     * data : [{"id":18,"machineNo":"#00004","carType":2,"pillarColor":"red","longitude":114.561064,"latitude":21.192861,"areaCode":"FST","address":"yyy地址","parkingSpaces":[],"distance":""},{"id":17,"machineNo":"#00003","carType":1,"pillarColor":"gray","longitude":113.560966,"latitude":22.191443,"areaCode":"DT","address":"百德大厦","parkingSpaces":["01","02","03","04"],"distance":null},{"id":15,"machineNo":"#00001","carType":2,"pillarColor":"bule","longitude":113.559394,"latitude":22.191648,"areaCode":"DT","address":"xxx地址","parkingSpaces":null,"distance":null},{"id":16,"machineNo":"#00002","carType":2,"pillarColor":"yellow","longitude":113.561064,"latitude":22.192861,"areaCode":"DT","address":"艾维斯写字楼","parkingSpaces":null,"distance":null}]
     */

    private int code;
    private String msg;
    private List<TestSlotMachineModel> data;

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

    public List<TestSlotMachineModel> getData() {
        return data;
    }

    public void setData(List<TestSlotMachineModel> data) {
        this.data = data;
    }

    public static class TestSlotMachineModel {
        /**
         * id : 18
         * machineNo : #00004
         * carType : 2
         * pillarColor : red
         * longitude : 114.561064
         * latitude : 21.192861
         * areaCode : FST
         * address : yyy地址
         * parkingSpaces : []
         * distance :
         */

        private int id;
        private String machineNo;
        private int carType;
        private String pillarColor;
        private double longitude;
        private double latitude;
        private String areaCode;
        private String address;
        private String distance;
        private List<String> parkingSpaces;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMachineNo() {
            return machineNo;
        }

        public void setMachineNo(String machineNo) {
            this.machineNo = machineNo;
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

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public List<String> getParkingSpaces() {
            return parkingSpaces;
        }

        public void setParkingSpaces(List<String> parkingSpaces) {
            this.parkingSpaces = parkingSpaces;
        }
    }
}
