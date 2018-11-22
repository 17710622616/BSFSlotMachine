package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John_Li on 22/11/2018.
 */

public class RequestSecondCarModel {
    private int sellerId;
    private int carType;
    private String carBrand;
    private String type;
    private int year;
    private int carGears;
    private double startPrice;
    private double endPrice;
    private int ifperson;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCarGears() {
        return carGears;
    }

    public void setCarGears(int carGears) {
        this.carGears = carGears;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    public int getIfperson() {
        return ifperson;
    }

    public void setIfperson(int ifperson) {
        this.ifperson = ifperson;
    }
}
