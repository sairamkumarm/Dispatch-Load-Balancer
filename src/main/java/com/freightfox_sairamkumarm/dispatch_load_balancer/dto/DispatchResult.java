package com.freightfox_sairamkumarm.dispatch_load_balancer.dto;

import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class DispatchResult {
    private int totalOrders;
    private String averageDistance;
    private String totalDistance;
    private int totalLoad;
    private List<DispatchPlan> dispatchPlan;
    private List<Order> unassignedOrders;

    public void setAverageDistance(int averageDistance){
        this.averageDistance = averageDistance + " km";
    }

    public void setTotalDistance(int totalDistance){
        this.totalDistance = totalDistance + " km";
    }
}
