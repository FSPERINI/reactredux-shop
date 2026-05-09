package com.ecommerce.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

    @NotNull(message = "Delivery info is required")
    @Valid
    private DeliveryInfo deliveryInfo;

    public CreateOrderRequest() {}

    public DeliveryInfo getDeliveryInfo() { return deliveryInfo; }
    public void setDeliveryInfo(DeliveryInfo deliveryInfo) { this.deliveryInfo = deliveryInfo; }
}
