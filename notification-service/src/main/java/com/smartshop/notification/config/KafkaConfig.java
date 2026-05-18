package com.smartshop.notification.config;

import com.smartshop.notification.event.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization
        .StringDeserializer;
import org.springframework.beans.factory.annotation
        .Value;
import org.springframework.context.annotation
        .Bean;
import org.springframework.context.annotation
        .Configuration;
import org.springframework.kafka.config
        .ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core
        .ConsumerFactory;
import org.springframework.kafka.core
        .DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer
        .JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private <T> ConsumerFactory<String, T>
    consumerFactory(Class<T> targetType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "notification-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(targetType, false));
    }

    private <T>
    ConcurrentKafkaListenerContainerFactory<String, T>
    factory(Class<T> targetType) {
        var factory =
                new ConcurrentKafkaListenerContainerFactory
                        <String, T>();
        factory.setConsumerFactory(
                consumerFactory(targetType));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
            <String, OrderCreatedEvent>
    orderKafkaListenerFactory() {
        return factory(OrderCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
            <String, PaymentSuccessEvent>
    paymentKafkaListenerFactory() {
        return factory(PaymentSuccessEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
            <String, LowStockEvent>
    lowStockKafkaListenerFactory() {
        return factory(LowStockEvent.class);
    }
}