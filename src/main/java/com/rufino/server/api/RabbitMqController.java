package com.rufino.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.DeliveryOrder;
import com.rufino.server.services.DeliveryOrderService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqController {

    private DeliveryOrderService deliveryOrderService;

    public RabbitMqController(DeliveryOrderService deliveryOrderService) {
        this.deliveryOrderService = deliveryOrderService;
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void recievedMessage(String incomingMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DeliveryOrder deliveryOrder = objectMapper.readValue(incomingMessage, DeliveryOrder.class);
            deliveryOrderService.addDelivery(deliveryOrder);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // deliveryOrderService.addDelivery(incomingMessage);
        System.out.println("Recieved Message From RabbitMQ: " + incomingMessage);

    }
}