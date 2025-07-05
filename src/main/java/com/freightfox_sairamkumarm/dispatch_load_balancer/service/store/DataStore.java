package com.freightfox_sairamkumarm.dispatch_load_balancer.service.store;

import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStore {
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();
    private final Map<String, Vehicle> vehicleStore = new ConcurrentHashMap<>();

    public void saveOrder(Order o){
        orderStore.put(o.getOrderId(), o);
    }

    public void saveAllOrders(List<Order> orders){
        orders.forEach(o -> orderStore.put(o.getOrderId(),o));
    }

    public void saveVehicle(Vehicle v){
        vehicleStore.put(v.getVehicleId(), v);
    }
    public void saveAllVehicles(List<Vehicle> vehicles) {
        vehicles.forEach(v -> vehicleStore.put(v.getVehicleId(), v));
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderStore.values());
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicleStore.values());
    }

    public void clear() {
        orderStore.clear();
        vehicleStore.clear();
    }
}
