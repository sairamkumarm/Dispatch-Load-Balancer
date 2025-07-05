package com.freightfox_sairamkumarm.dispatch_load_balancer.model;

import lombok.Data;

@Data
public class Order {

    public enum OrderPriority {
        HIGH,
        MEDIUM,
        LOW
    }

    private String orderId;
    private Double latitude;
    private Double longitude;
    private String address;
    private int packageWeight;
    private OrderPriority priority;


    public void setOrderId(String orderId) {
        this.orderId = orderId.toUpperCase();
    }

    public Order(){}
}
