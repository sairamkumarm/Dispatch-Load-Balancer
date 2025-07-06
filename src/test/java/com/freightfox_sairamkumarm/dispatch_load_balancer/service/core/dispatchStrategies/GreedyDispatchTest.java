package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.dispatchStrategies;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchPlan;
import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.DispatchResult;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order.OrderPriority.*;
import static org.junit.jupiter.api.Assertions.*;

class GreedyDispatchTest {

    private final GreedyDispatch strategy = new GreedyDispatch();

    @Test
    @DisplayName("Assigns orders within capacity and honors priority")
    void testBasicDispatchAssignment() {
        List<Vehicle> vehicles = List.of(
                buildVehicle("V1", 100, 28.6139, 77.2090)  // Connaught Place
        );

        List<Order> orders = List.of(
                buildOrder("O1", 28.6139, 77.2090, 30, HIGH),
                buildOrder("O2", 28.6139, 77.2090, 40, MEDIUM),
                buildOrder("O3", 28.6139, 77.2090, 50, LOW)  // This one should be dropped due to capacity
        );

        DispatchResult result = strategy.generateDispatchPlans(orders, vehicles);

        assertEquals(2, result.getTotalOrders());
        assertEquals(70, result.getTotalLoad());
        assertEquals(1, result.getDispatchPlan().size());
        assertEquals("V1", result.getDispatchPlan().getFirst().getVehicleId());

        List<Order> assigned = result.getDispatchPlan().getFirst().getAssignedOrders();
        assertEquals(2, assigned.size());
        assertTrue(assigned.stream().anyMatch(o -> o.getOrderId().equals("O1")));
        assertTrue(assigned.stream().anyMatch(o -> o.getOrderId().equals("O2")));

        List<Order> unassigned = result.getUnassignedOrders();
        assertEquals(1, unassigned.size());
        assertEquals("O3", unassigned.getFirst().getOrderId());
    }

    @Test
    @DisplayName("Returns empty plan when no orders provided")
    void testEmptyOrderList() {
        List<Vehicle> vehicles = List.of(buildVehicle("V1", 100, 28.6139, 77.2090));
        DispatchResult result = strategy.generateDispatchPlans(List.of(), vehicles);

        assertEquals(0, result.getTotalOrders());
        assertEquals(0, result.getTotalLoad());
        assertTrue(result.getDispatchPlan().stream().allMatch(dp -> dp.getAssignedOrders().isEmpty()));
        assertTrue(result.getUnassignedOrders().isEmpty());
    }

    @Test
    @DisplayName("All orders unassigned when no vehicles available")
    void testNoVehiclesAvailable() {
        List<Order> orders = List.of(
                buildOrder("O1", 28.6, 77.2, 10, HIGH),
                buildOrder("O2", 28.6, 77.2, 20, MEDIUM)
        );
        DispatchResult result = strategy.generateDispatchPlans(orders, List.of());

        assertEquals(0, result.getTotalLoad());
        assertEquals(0, result.getDispatchPlan().size());
        assertEquals(2, result.getUnassignedOrders().size());
    }

    @Test
    @DisplayName("Correct distance and load calculation")
    void testDistanceAndLoad() {
        List<Vehicle> vehicles = List.of(buildVehicle("V1", 50, 12.9716, 77.6413));
        List<Order> orders = List.of(
                buildOrder("O1", 12.9716, 77.5946, 50, HIGH)
        );

        DispatchResult result = strategy.generateDispatchPlans(orders, vehicles);

        assertEquals(1, result.getDispatchPlan().size());
        DispatchPlan plan = result.getDispatchPlan().getFirst();

        assertEquals(50, plan.getTotalLoad());
        assertEquals("V1", plan.getVehicleId());
        assertEquals(1, plan.getAssignedOrders().size());
        assertTrue(plan.getTotalDistance().endsWith(" km"));
        assertEquals(5,Integer.parseInt(plan.getTotalDistance().replace(" km","")));
    }

    // ——————————————————————————————————————————————————
    // Helpers to build test data
    // ——————————————————————————————————————————————————

    private Vehicle buildVehicle(String id, int capacity, double lat, double lon) {
        Vehicle v = new Vehicle();
        v.setVehicleId(id);
        v.setCapacity(capacity);
        v.setCurrentLatitude(lat);
        v.setCurrentLongitude(lon);
        v.setCurrentAddress("Test Address");
        return v;
    }

    private Order buildOrder(String id, double lat, double lon, int weight, Order.OrderPriority priority) {
        Order o = new Order();
        o.setOrderId(id);
        o.setLatitude(lat);
        o.setLongitude(lon);
        o.setAddress("Test Address");
        o.setPackageWeight(weight);
        o.setPriority(priority);
        return o;
    }
}