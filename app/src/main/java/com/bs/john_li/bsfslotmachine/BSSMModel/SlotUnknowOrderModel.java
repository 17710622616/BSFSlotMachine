package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 未知咪錶的實體類
 * Created by John_Li on 20/10/2017.
 */

public class SlotUnknowOrderModel {
    private String slotAmount;
    private String carId;
    private int carType;
    private String pillarColor;
    private String areaCode;
    private String startSlotTime;
    private String unknowMachineno;

    private String remark;

    public String getUnknowMachineno() {
        return unknowMachineno;
    }

    public void setUnknowMachineno(String unknowMachineno) {
        this.unknowMachineno = unknowMachineno;
    }

    public String getSlotAmount() {
        return slotAmount;
    }

    public void setSlotAmount(String slotAmount) {
        this.slotAmount = slotAmount;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public String getStartSlotTime() {
        return startSlotTime;
    }

    public void setStartSlotTime(String startSlotTime) {
        this.startSlotTime = startSlotTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
