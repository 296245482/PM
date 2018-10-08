package com.huawei.entity;

import java.io.Serializable;
import java.util.Date;

public class DeviceNewData implements Serializable, Cloneable {
    private Integer id;
    private String shortDeviceId;
    private String deviceId;
    private double pm25;
    private double temp;
    private double RH;
    private double CO;
    private double CO2;
    private double NO2;
    private double O3;
    private double VOC;
    private Date timePoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDeviceId() {
        return shortDeviceId;
    }

    public void setShortDeviceId(String shortDeviceId) {
        this.shortDeviceId = shortDeviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getRH() {
        return RH;
    }

    public void setRH(double RH) {
        this.RH = RH;
    }

    public double getCO() {
        return CO;
    }

    public void setCO(double CO) {
        this.CO = CO;
    }

    public double getCO2() {
        return CO2;
    }

    public void setCO2(double CO2) {
        this.CO2 = CO2;
    }

    public double getNO2() {
        return NO2;
    }

    public void setNO2(double NO2) {
        this.NO2 = NO2;
    }

    public double getO3() {
        return O3;
    }

    public void setO3(double o3) {
        O3 = o3;
    }

    public double getVOC() {
        return VOC;
    }

    public void setVOC(double VOC) {
        this.VOC = VOC;
    }

    public Date getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Date timePoint) {
        this.timePoint = timePoint;
    }
}