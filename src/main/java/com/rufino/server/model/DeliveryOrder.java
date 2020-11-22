package com.rufino.server.model;

import java.util.UUID;

public class DeliveryOrder {

    private UUID idDelivery;
    private String idClient;
    private String orderAddress;

    public UUID getIdDelivery() {
        return idDelivery;
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

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }   
    
}
