package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.dispatchStrategies;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchPlan;
import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchResult;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.DispatchStrategy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("greedy")
public class GreedyDispatch implements DispatchStrategy {
    private static final double R = 6371; // Earth radius in KM
    private static final Map<String, Double> distMap = new HashMap<>();
    @Override
    public DispatchResult generateDispatchPlans(List<Order> orders, List<Vehicle> vehicles) {
        Comparator<Order> comparator = Comparator
                .comparingInt((Order o) -> o.getPriority().ordinal())
                .thenComparingInt(o -> -o.getPackageWeight());

        PriorityQueue<Order> orderQueue = new PriorityQueue<>(comparator);
        orderQueue.addAll(orders);

        vehicles.sort(Comparator.comparingInt(Vehicle::getCapacity).reversed());

        Map<String, DispatchPlan> planMap = new HashMap<>();
        Map<String, Integer> remainingCap = new HashMap<>();
        Map<String, double[]> vehicleLoc = new HashMap<>();

        for (Vehicle v : vehicles) {
            planMap.put(v.getVehicleId(), new DispatchPlan());
            planMap.get(v.getVehicleId()).setVehicleId(v.getVehicleId());
            planMap.get(v.getVehicleId()).setAssignedOrders(new ArrayList<>());
            remainingCap.put(v.getVehicleId(), v.getCapacity());
            vehicleLoc.put(v.getVehicleId(), new double[]{v.getCurrentLatitude(), v.getCurrentLongitude()});
        }

        List<Order> unassignedOrders = new ArrayList<>();

        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();

            String bestVehicleId = null;
            double bestDistance = Double.MAX_VALUE;

            for (Vehicle v : vehicles) {
                int cap = remainingCap.get(v.getVehicleId());
                if (cap >= order.getPackageWeight()) {
                    double[] loc = vehicleLoc.get(v.getVehicleId());
                    double dist = distance(loc[0], loc[1], order.getLatitude(), order.getLongitude());
                    if (dist < bestDistance) {
                        bestDistance = dist;
                        bestVehicleId = v.getVehicleId();
                    }
                }
            }

            if (bestVehicleId == null) {
                unassignedOrders.add(order);
                continue;
            }

            DispatchPlan plan = planMap.get(bestVehicleId);
            plan.getAssignedOrders().add(order);
            plan.setTotalLoad((double) (plan.getTotalLoad() + order.getPackageWeight()));
            double updatedDistance = Double.parseDouble(plan.getTotalDistance().replace(" km", "")) + bestDistance;
            plan.setTotalDistance(updatedDistance);

            remainingCap.put(bestVehicleId, remainingCap.get(bestVehicleId) - order.getPackageWeight());
            vehicleLoc.put(bestVehicleId, new double[]{order.getLatitude(), order.getLongitude()});
        }
        return buildResult(new ArrayList<>(planMap.values()), unassignedOrders);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        String key = lat1 + "," + lon1 + "|" + lat2 + "," + lon2;
        return distMap.computeIfAbsent(key, k -> {
            double lat1Rad = Math.toRadians(lat1);
            double lon1Rad = Math.toRadians(lon1);
            double lat2Rad = Math.toRadians(lat2);
            double lon2Rad = Math.toRadians(lon2);

            double dLat = lat2Rad - lat1Rad;
            double dLon = lon2Rad - lon1Rad;

            double a = Math.pow(Math.sin(dLat / 2), 2)
                    + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);
            double c = 2 * Math.asin(Math.sqrt(a));
            return R * c;
        });
    }
}
