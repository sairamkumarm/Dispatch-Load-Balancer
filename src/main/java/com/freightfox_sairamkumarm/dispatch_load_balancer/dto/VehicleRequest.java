package com.freightfox_sairamkumarm.dispatch_load_balancer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequest {
    @NotBlank
    private String vehicleId;

    @Min(1)
    private Double capacity;

    @NotNull
    private Double currentLatitude;

    @NotNull
    private Double currentLongitude;

    @NotBlank
    private String currentAddress;
}
