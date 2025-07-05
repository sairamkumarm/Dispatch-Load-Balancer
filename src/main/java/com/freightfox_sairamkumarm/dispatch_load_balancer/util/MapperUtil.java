package com.freightfox_sairamkumarm.dispatch_load_balancer.util;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.OrderListRequest;
import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.VehicleListRequest;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {

    public static List<Order> toModelOrderList(OrderListRequest orderListRequest){
        return orderListRequest.getOrders().stream().map(orderRequest-> {
            Order o = new Order();
            o.setOrderId(orderRequest.getOrderId());
            o.setPackageWeight(orderRequest.getPackageWeight().intValue());
            o.setLatitude(orderRequest.getLatitude());
            o.setLongitude(orderRequest.getLongitude());
            o.setAddress(orderRequest.getAddress());
            o.setPriority(orderRequest.getPriority());
            return o;
        }).collect(Collectors.toList());
    }

    public static List<Vehicle> toModelVehicleList(VehicleListRequest vehicleListRequest) {
        return vehicleListRequest.getVehicles().stream().map(dto -> {
            Vehicle v = new Vehicle();
            v.setVehicleId(dto.getVehicleId());
            v.setCurrentLatitude(dto.getCurrentLatitude());
            v.setCurrentLongitude(dto.getCurrentLongitude());
            v.setCurrentAddress(dto.getCurrentAddress());
            v.setCapacity(dto.getCapacity().intValue());
            return v;
        }).collect(Collectors.toList());
    }
}
