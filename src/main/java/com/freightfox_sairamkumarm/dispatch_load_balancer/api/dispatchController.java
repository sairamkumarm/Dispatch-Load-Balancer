package com.freightfox_sairamkumarm.dispatch_load_balancer.api;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.*;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Vehicle;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.DispatchService;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.PlanService;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.core.VehicleService;
import com.freightfox_sairamkumarm.dispatch_load_balancer.service.store.DataStore;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/dispatch")
@RestController
public class dispatchController {

    private final DispatchService dispatchService;
    private final VehicleService vehicleService;
    private final PlanService planService;
    private final DataStore dataStore;

    public dispatchController(DispatchService dispatchService, VehicleService vehicleService, PlanService planService, DataStore dataStore) {
        this.dispatchService = dispatchService;
        this.vehicleService = vehicleService;
        this.planService = planService;
        this.dataStore = dataStore;
    }


    @PostMapping("/orders")
    public ResponseEntity<ResponseDTO<List<Order>>> takeOrders(@Valid @RequestBody OrderListRequest orderListRequest){
        dispatchService.takeOrders(orderListRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Delivery orders accepted", "success",dataStore.getAllOrders()));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ResponseDTO<List<Vehicle>>> takeVehicles(@Valid @RequestBody VehicleListRequest vehicleListRequest){
        vehicleService.addVehicles(vehicleListRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Delivery vehicles accepted", "success",dataStore.getAllVehicles()));
    }

    @GetMapping("/fullPlan")
    public ResponseEntity<ResponseDTO<DispatchResult>> getFullPlans(){
        DispatchResult res = planService.generatePlans();
        return ResponseEntity.ok(new ResponseDTO<>("Full Plan created", "Success", res));
    }

    @GetMapping("/plan")
    public ResponseEntity<ResponseDTO<List<DispatchPlan>>> getPlans(){
        List<DispatchPlan> res = planService.generatePlans().getDispatchPlan();
        return ResponseEntity.ok(new ResponseDTO<>("Plan created", "Success", res));
    }

}
