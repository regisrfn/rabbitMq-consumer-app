package com.rufino.server.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.DeliveryDao;
import com.rufino.server.model.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("DB_H2")
public class DeliveryOrderRepository implements DeliveryDao {

    private JdbcTemplate jdbcTemplate;
    private List<Delivery> deliveryList;

    @Autowired
    public DeliveryOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.deliveryList = new ArrayList<>();
    }

    @Override
    public int insert(UUID id, Delivery deliveryOrder) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO delivery " + "(id_delivery, id_order, order_address)" + "VALUES (?, ?, ?)", id,
                    deliveryOrder.getIdOrder(), deliveryOrder.getOrderAddress());
            deliveryOrder.setIdDelivery((result > 0 ? id : null));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Delivery> getAllDelivery() {
        try {
            String sql = "SELECT * FROM DELIVERY";
            deliveryList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Delivery>(Delivery.class));
            return deliveryList;
        } catch (Exception e) {
            e.printStackTrace();
            return deliveryList;
        }
    }

}
