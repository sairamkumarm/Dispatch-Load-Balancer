package com.freightfox_sairamkumarm.dispatch_load_balancer.service.core;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.VehicleListRequest;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.store.DataStore;
import com.freightfox_sairamkumarm.dispatch_load_balancer.util.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final DataStore dataStore;

    public VehicleService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addVehicles(VehicleListRequest vehicleListRequest){
        try {
            dataStore.saveAllVehicles(MapperUtil.toModelVehicleList(vehicleListRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
