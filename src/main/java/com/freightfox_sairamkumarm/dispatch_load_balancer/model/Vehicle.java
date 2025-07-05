package com.freightfox_sairamkumarm.dispatch_load_balancer.model;

import lombok.Data;

@Data
public class Vehicle {
    private String vehicleId;
    private int capacity;
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddress;

    public Vehicle(){}

    public void setVehicleId(String vehicleId){
        this.vehicleId = vehicleId.toUpperCase();
    }
}
