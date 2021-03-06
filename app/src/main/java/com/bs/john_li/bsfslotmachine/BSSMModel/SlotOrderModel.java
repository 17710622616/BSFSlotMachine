package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 停車訂單
 * Created by John_Li on 4/10/2017.
 */

public class SlotOrderModel {
    private String slotAmount;
    private String machineNo;
    private long carId;
    private String startSlotTime;
    private String remark;
    private String parkingSpace;

    public String getEndSlotTime() {
        return endSlotTime;
    }

    public void setEndSlotTime(String endSlotTime) {
        this.endSlotTime = endSlotTime;
    }

    private String endSlotTime;

    public String getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(String parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSlotAmount() {
        return slotAmount;
    }

    public void setSlotAmount(String slotAmount) {
        this.slotAmount = slotAmount;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public String getStartSlotTime() {
        return startSlotTime;
    }

    public void setStartSlotTime(String startSlotTime) {
        this.startSlotTime = startSlotTime;
    }
}
