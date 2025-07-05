package com.freightfox_sairamkumarm.dispatch_load_balancer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleListRequest {
    @NotEmpty
    private List<@Valid VehicleRequest> vehicles;
}
