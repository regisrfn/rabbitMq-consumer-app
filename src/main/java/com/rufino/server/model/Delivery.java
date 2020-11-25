package com.rufino.server.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "idDelivery", "idOrder","orderAddress" })
public class Delivery {

    private UUID idDelivery;
    private UUID idOrder;
    private String orderAddress;

    public UUID getIdDelivery() {
        return idDelivery;
    }

    public UUID getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(UUID idOrder) {
        this.idOrder = idOrder;
    }

    public void setIdDelivery(UUID idDelivery) {
        this.idDelivery = idDelivery;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
    
}
