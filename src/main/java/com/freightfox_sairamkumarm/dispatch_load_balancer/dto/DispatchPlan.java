package com.freightfox_sairamkumarm.dispatch_load_balancer.dto;

import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class DispatchPlan {
    private String vehicleId;
    private int totalLoad;
    private String totalDistance;
    private List<Order> assignedOrders;

    public void setTotalLoad(Double totalLoad){
        this.totalLoad = totalLoad.intValue();
    }

    public void setTotalDistance(Double totalDistance){
        this.totalDistance = totalDistance.intValue() + " km";
    }

    public DispatchPlan(){
        this.totalDistance = "0 km";
    }
}
