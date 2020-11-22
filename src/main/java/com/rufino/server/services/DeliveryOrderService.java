package com.rufino.server.services;

import com.rufino.server.dao.DeliveryOrderDao;
import com.rufino.server.model.DeliveryOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeliveryOrderService {

    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    public DeliveryOrderService(@Qualifier("DB_H2") DeliveryOrderDao deliveryOrderDao) {
        this.deliveryOrderDao = deliveryOrderDao;
    }

    public int addDelivery(DeliveryOrder deliveryOrder){
        return deliveryOrderDao.insertOrder(deliveryOrder);
    }



    
}
