package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * 咪錶實體類
 * Created by John_Li on 21/9/2017.
 */

public class SlotMachineListOutsideModel {

    /**
     * code : 200
     * msg : null
     * data : {"totalCount":3,"data":[{"id":16,"machineNo":"#00002","carType":2,"pillarColor":"yellow","longitude":113.561064,"latitude":22.192861,"areaCode":"DT","address":"艾维斯写字楼","parkingSpaces":null,"distance":218.5458}]}
     */

    private int code;
    private String msg;
    private SlotMachineListModel data;

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

    public SlotMachineListModel getData() {
        return data;
    }

    public void setData(SlotMachineListModel data) {
        this.data = data;
    }

    public static class SlotMachineListModel {
        /**
         * totalCount : 3
         * data : [{"id":16,"machineNo":"#00002","carType":2,"pillarColor":"yellow","longitude":113.561064,"latitude":22.192861,"areaCode":"DT","address":"艾维斯写字楼","parkingSpaces":null,"distance":218.5458}]
         */

        private int totalCount;
        private List<SlotMachineModel> data;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<SlotMachineModel> getData() {
            return data;
        }

        public void setData(List<SlotMachineModel> data) {
            this.data = data;
        }

        public static class SlotMachineModel {
            /**
             * id : 16
             * machineNo : #00002
             * carType : 2
             * pillarColor : yellow
             * longitude : 113.561064
             * latitude : 22.192861
             * areaCode : DT
             * address : 艾维斯写字楼
             * parkingSpaces : null
             * distance : 218.5458
             */

            private int id;
            private String machineNo;
            private int carType;
            private String pillarColor;
            private double longitude;
            private double latitude;
            private String areaCode;
            private String address;
            private List<String> parkingSpaces;
            private double distance;

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

            public List<String> getParkingSpaces() {
                return parkingSpaces;
            }

            public void setParkingSpaces(List<String> parkingSpaces) {
                this.parkingSpaces = parkingSpaces;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }
        }
    }
}
