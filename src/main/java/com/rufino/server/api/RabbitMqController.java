package com.rufino.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.Delivery;
import com.rufino.server.services.DeliveryService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqController {

    private DeliveryService deliveryService;

    public RabbitMqController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void receivedMessage(String incomingMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Delivery delivery = objectMapper.readValue(incomingMessage, Delivery.class);
            deliveryService.addDelivery(delivery);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // deliveryService.addDelivery(incomingMessage);
        System.out.println("Received Message From RabbitMQ: " + incomingMessage);

    }
}