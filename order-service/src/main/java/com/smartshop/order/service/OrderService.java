package com.smartshop.order.service;

import com.smartshop.order.dto.*;
import com.smartshop.order.entity.Order;
import com.smartshop.order.entity.OrderItem;
import com.smartshop.order.event.OrderCreatedEvent;
import com.smartshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Order createOrder(
            CreateOrderRequest request) {

        List<OrderItem> items = request.getItems()
                .stream().map(itemReq ->
                        OrderItem.builder()
                                .productId(itemReq.getProductId())
                                .productName(itemReq.getProductName())
                                .quantity(itemReq.getQuantity())
                                .price(itemReq.getPrice())
                                .build()
                ).collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(
                        BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(request.getUserId())
                .orderNumber("SS-" + UUID.randomUUID()
                        .toString().substring(0, 8)
                        .toUpperCase())
                .totalAmount(total)
                .shippingAddress(request.getShippingAddress())
                .status(Order.OrderStatus.PENDING)
                .build();

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        Order saved = orderRepository.save(order);
        log.info("Order created: {}",
                saved.getOrderNumber());

        List<String> productIds = items.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        saved.getId(),
                        saved.getOrderNumber(),
                        saved.getUserId(),
                        saved.getTotalAmount(),
                        productIds);

        // Replace the kafka send line with:
        try {
            kafkaTemplate.send("order-created", event);
            log.info("Order event published: {}",
                    saved.getOrderNumber());
        } catch(Exception e) {
            log.warn("Kafka not available — order saved " +
                            "but event not published: {}",
                    e.getMessage());
        }

        return saved;
    }

    public List<Order> getOrdersByUser(
            String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Order not found: " + id));
    }

    public Order getOrderByNumber(
            String orderNumber) {
        return orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Order not found: " + orderNumber));
    }

    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        if(order.getStatus() ==
                Order.OrderStatus.DELIVERED)
            throw new RuntimeException(
                    "Cannot cancel delivered order");
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateStatus(Long id,
                              Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}