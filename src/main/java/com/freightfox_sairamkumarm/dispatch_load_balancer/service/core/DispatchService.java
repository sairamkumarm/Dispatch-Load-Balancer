package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.OrderListRequest;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.store.DataStore;
import com.freightfox_sairamkumarm.dispatch_load_balancer.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DispatchService {

    private final DataStore dataStore;

    public DispatchService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void takeOrders(OrderListRequest orderListRequest){
        try {
            List<Order> orders = MapperUtil.toModelOrderList(orderListRequest);
            dataStore.saveAllOrders(orders);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
