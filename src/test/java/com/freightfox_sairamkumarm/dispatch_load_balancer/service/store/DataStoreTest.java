package com.freightfox_sairamkumarm.dispatch_load_balancer.service.store;


import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order.OrderPriority.HIGH;
import static org.junit.jupiter.api.Assertions.*;

class DataStoreTest {

    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
    }

    @Test
    @DisplayName("saveOrder and getAllOrders should store a single order")
    void testSaveSingleOrder() {
        Order order = buildOrder("ORD001", 10);
        dataStore.saveOrder(order);

        List<Order> all = dataStore.getAllOrders();
        assertEquals(1, all.size());
        assertEquals("ORD001", all.get(0).getOrderId());
    }

    @Test
    @DisplayName("saveAllOrders should store multiple orders")
    void testSaveAllOrders() {
        Order o1 = buildOrder("ORD001", 10);
        Order o2 = buildOrder("ORD002", 20);

        dataStore.saveAllOrders(List.of(o1, o2));

        List<Order> all = dataStore.getAllOrders();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(o -> o.getOrderId().equals("ORD001")));
        assertTrue(all.stream().anyMatch(o -> o.getOrderId().equals("ORD002")));
    }

    @Test
    @DisplayName("saveVehicle and getAllVehicles should store a single vehicle")
    void testSaveSingleVehicle() {
        Vehicle v = buildVehicle("VEH001", 100);
        dataStore.saveVehicle(v);

        List<Vehicle> all = dataStore.getAllVehicles();
        assertEquals(1, all.size());
        assertEquals("VEH001", all.get(0).getVehicleId());
    }

    @Test
    @DisplayName("saveAllVehicles should store multiple vehicles")
    void testSaveAllVehicles() {
        Vehicle v1 = buildVehicle("VEH001", 100);
        Vehicle v2 = buildVehicle("VEH002", 120);

        dataStore.saveAllVehicles(List.of(v1, v2));

        List<Vehicle> all = dataStore.getAllVehicles();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(v -> v.getVehicleId().equals("VEH001")));
        assertTrue(all.stream().anyMatch(v -> v.getVehicleId().equals("VEH002")));
    }

    @Test
    @DisplayName("clear should wipe both stores")
    void testClear() {
        dataStore.saveOrder(buildOrder("ORD001", 10));
        dataStore.saveVehicle(buildVehicle("VEH001", 100));

        dataStore.clear();

        assertTrue(dataStore.getAllOrders().isEmpty());
        assertTrue(dataStore.getAllVehicles().isEmpty());
    }

    // ——————————————————————————————————————————————————
    // Helpers
    // ——————————————————————————————————————————————————

    private Order buildOrder(String id, int weight) {
        Order o = new Order();
        o.setOrderId(id);
        o.setPackageWeight(weight);
        o.setLatitude(28.0);
        o.setLongitude(77.0);
        o.setAddress("Sample Address");
        o.setPriority(HIGH);
        return o;
    }

    private Vehicle buildVehicle(String id, int capacity) {
        Vehicle v = new Vehicle();
        v.setVehicleId(id);
        v.setCapacity(capacity);
        v.setCurrentLatitude(28.0);
        v.setCurrentLongitude(77.0);
        v.setCurrentAddress("Sample Address");
        return v;
    }
}