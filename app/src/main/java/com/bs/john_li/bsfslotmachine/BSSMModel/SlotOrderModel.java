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
