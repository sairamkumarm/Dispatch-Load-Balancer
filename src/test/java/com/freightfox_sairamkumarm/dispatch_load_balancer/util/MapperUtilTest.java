package com.freightfox_sairamkumarm.dispatch_load_balancer.util;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.*;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order.OrderPriority.*;
import static org.junit.jupiter.api.Assertions.*;

class MapperUtilTest {

    /* ———————————————————————————  ORDER  ——————————————————————————— */

    @Test
    @DisplayName("toModelOrderList maps every field and upper‑cases IDs")
    void testOrderMapping() {
        // Given DTO list
        OrderRequest dto = new OrderRequest();
        dto.setOrderId("ord123");
        dto.setLatitude(12.9716);
        dto.setLongitude(77.5946);
        dto.setAddress("MG Road");
        dto.setPackageWeight(10.0);
        dto.setPriority(HIGH);

        OrderListRequest listRequest = new OrderListRequest();
        listRequest.setOrders(List.of(dto));

        // When
        List<Order> models = MapperUtil.toModelOrderList(listRequest);

        // Then
        assertEquals(1, models.size());
        Order m = models.get(0);
        assertEquals("ORD123", m.getOrderId());                 // upper‑cased
        assertEquals(12.9716, m.getLatitude());
        assertEquals(77.5946, m.getLongitude());
        assertEquals("MG Road", m.getAddress());
        assertEquals(10, m.getPackageWeight());                 // cast → int
        assertEquals(HIGH, m.getPriority());
    }

    @Test
    @DisplayName("toModelOrderList returns empty list for empty request")
    void testEmptyOrderList() {
        OrderListRequest empty = new OrderListRequest();
        empty.setOrders(List.of());
        assertTrue(MapperUtil.toModelOrderList(empty).isEmpty());
    }

    /* ———————————————————————————  VEHICLE  ——————————————————————————— */

    @Test
    @DisplayName("toModelVehicleList maps every field and upper‑cases IDs")
    void testVehicleMapping() {
        VehicleRequest vDto = new VehicleRequest();
        vDto.setVehicleId("veh001");
        vDto.setCapacity(80.0);
        vDto.setCurrentLatitude(28.6139);
        vDto.setCurrentLongitude(77.2090);
        vDto.setCurrentAddress("Connaught");

        VehicleListRequest vList = new VehicleListRequest();
        vList.setVehicles(List.of(vDto));

        List<Vehicle> models = MapperUtil.toModelVehicleList(vList);

        assertEquals(1, models.size());
        Vehicle v = models.get(0);

        assertEquals("VEH001", v.getVehicleId());               // upper‑cased
        assertEquals(80, v.getCapacity());                      // cast → int
        assertEquals(28.6139, v.getCurrentLatitude());
        assertEquals(77.2090, v.getCurrentLongitude());
        assertEquals("Connaught", v.getCurrentAddress());
    }

    @Test
    @DisplayName("toModelVehicleList returns empty list for empty request")
    void testEmptyVehicleList() {
        VehicleListRequest empty = new VehicleListRequest();
        empty.setVehicles(List.of());
        assertTrue(MapperUtil.toModelVehicleList(empty).isEmpty());
    }
}