package com.rufino.server.services;

import java.util.List;

import com.rufino.server.dao.DeliveryDao;
import com.rufino.server.model.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService{

    private DeliveryDao deliveryDao;

    @Autowired
    public DeliveryService(@Qualifier("DB_H2") DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
    }

    public int addDelivery(Delivery delivery){
        return deliveryDao.insertOrder(delivery);
    }

    public List<Delivery> getAll(){
        return deliveryDao.getAllDelivery();
    }
    
}
