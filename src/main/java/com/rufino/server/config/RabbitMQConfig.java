package com.rufino.server.config;

import java.net.URI;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${sample.rabbitmq.queue}")
    String queueName;
    @Value("${sample.rabbitmq.exchange}")
    String exchange;
    @Value("${sample.rabbitmq.routingkey}")
    private String routingkey;

    @Bean
    public ConnectionFactory connectionFactory() {
        URI rabbitMq;
        String rabbitMqUrl = getEnv("CLOUDAMQP_URL");
        if (rabbitMqUrl == null) {
            rabbitMq = URI.create("amqp://guest:guest@localhost:5672");
        } else {
            rabbitMq = URI.create(rabbitMqUrl);
        }

        final CachingConnectionFactory factory = new CachingConnectionFactory(rabbitMq.getHost());
        factory.setUsername(rabbitMq.getUserInfo().split(":")[0]);
        factory.setPassword(rabbitMq.getUserInfo().split(":")[1]);
        factory.setPort(rabbitMq.getPort());
        if (!rabbitMq.getPath().isEmpty()) {
            factory.setVirtualHost(rabbitMq.getPath().substring(1));
        }
        return factory;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    private static String getEnv(String name) {
        final String env = System.getenv(name);
        if (env == null) {
            System.out.println("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}