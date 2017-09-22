package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * 咪錶實體類
 * Created by John_Li on 21/9/2017.
 */

public class SlotMachineListModel {

    /**
     * code : 200
     * msg : null
     * data : [{"id":17,"machineNo":"#00003","carType":1,"pillarColor":"gray","longitude":113.560966,"latitude":22.191443,"areaCode":"DT","address":"百德大厦","parkingSpaces":["01","02","03","04"],"distance":0.9785}]
     */

    private int code;
    private Object msg;
    private List<SlotMachineModel> data;

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

    public List<SlotMachineModel> getData() {
        return data;
    }

    public void setData(List<SlotMachineModel> data) {
        this.data = data;
    }

    public static class SlotMachineModel {
        /**
         * id : 17
         * machineNo : #00003
         * carType : 1
         * pillarColor : gray
         * longitude : 113.560966
         * latitude : 22.191443
         * areaCode : DT
         * address : 百德大厦
         * parkingSpaces : ["01","02","03","04"]
         * distance : 0.9785
         */

        private int id;
        private String machineNo;
        private int carType;
        private String pillarColor;
        private double longitude;
        private double latitude;
        private String areaCode;
        private String address;
        private double distance;
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

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
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
