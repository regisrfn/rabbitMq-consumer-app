package com.rufino.server.repository;

import java.util.UUID;

import com.rufino.server.dao.DeliveryOrderDao;
import com.rufino.server.model.DeliveryOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("DB_H2")
public class DeliveryOrderRepository implements DeliveryOrderDao {
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public DeliveryOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(UUID id, DeliveryOrder deliveryOrder) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO delivery " + "(id_delivery, id_client, order_address)"
                            + "VALUES (?, ?, ?)",
                    id, deliveryOrder.getIdClient(),deliveryOrder.getOrderAddress());
            deliveryOrder.setIdDelivery((result > 0 ? id : null));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }    
    
}
