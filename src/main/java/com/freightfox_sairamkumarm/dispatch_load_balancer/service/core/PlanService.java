package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchPlan;
import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchResult;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.store.DataStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    private final DispatchStrategy dispatchStrategy;
    private final DataStore dataStore;

    public PlanService(@Qualifier("greedy") DispatchStrategy dispatchStrategy, DataStore dataStore) {
        this.dispatchStrategy = dispatchStrategy;
        this.dataStore = dataStore;
    }

    public DispatchResult generatePlans(){
        List<Order> orders = dataStore.getAllOrders();
        List<Vehicle> vehicles = dataStore.getAllVehicles();
        return dispatchStrategy.generateDispatchPlans(orders,vehicles);
    }
}
