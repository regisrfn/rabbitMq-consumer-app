package com.rufino.server.dao;

import java.util.UUID;

import com.rufino.server.model.DeliveryOrder;


public interface DeliveryOrderDao {

    int insert(UUID id, DeliveryOrder deliveryOrder);

    default int insertOrder(DeliveryOrder order){
        UUID id = UUID.randomUUID();
        return insert(id, order);
    }
    
}
