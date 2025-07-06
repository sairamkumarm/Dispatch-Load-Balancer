package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchPlan;
import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchResult;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;

import java.util.List;

public interface DispatchStrategy {
    DispatchResult generateDispatchPlans(List<Order> orders, List<Vehicle> vehicles);

    default DispatchResult buildResult(List<DispatchPlan> plans, List<Order> unassigned) {
        DispatchResult result = new DispatchResult();
        result.setDispatchPlan(plans);
        result.setUnassignedOrders(unassigned);

        // totals & averages
        int totalOrders = plans.stream()
                .mapToInt(p -> p.getAssignedOrders().size())
                .sum();
        int totalLoad   = plans.stream()
                .mapToInt(DispatchPlan::getTotalLoad)
                .sum();
        int totalKm     = plans.stream()
                .mapToInt(p -> Integer.parseInt(
                        p.getTotalDistance().replace(" km", "")))
                .sum();

        result.setTotalOrders(totalOrders);
        result.setTotalLoad(totalLoad);
        result.setTotalDistance(totalKm);                 // setter appends " km" dont forget
        int avg = plans.isEmpty() ? 0 : totalKm / plans.size();
        result.setAverageDistance(avg);                   // setter appends " km"
        return result;
    }
}
