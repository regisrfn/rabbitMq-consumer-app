package com.rufino.server.dao;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Delivery;


public interface DeliveryDao {

    int insert(UUID id, Delivery deliveryOrder);
    List<Delivery> getAllDelivery();

    default int insertOrder(Delivery order){
        UUID id = UUID.randomUUID();
        return insert(id, order);
    }
    
}
