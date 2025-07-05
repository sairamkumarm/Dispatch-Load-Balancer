package com.freightfox_sairamkumarm.dispatch_load_balancer.dto;

import com.freightfox_sairamkumarm.dispatch_load_balancer.model.Order;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank
    private String orderId;

    @NotNull
    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    private Double longitude;

    @NotBlank
    private String address;

    @Min(1)
    private Double packageWeight;

    @NotNull
    private Order.OrderPriority priority;
}
