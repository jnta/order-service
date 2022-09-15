package com.jonata.orderservice.service;

import com.jonata.orderservice.dto.OrderItemsDto;
import com.jonata.orderservice.dto.OrderRequest;
import com.jonata.orderservice.dto.OrderResponse;
import com.jonata.orderservice.model.Order;
import com.jonata.orderservice.model.OrderItems;
import com.jonata.orderservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItems = orderRequest.getOrderItemsDtoList()
                .stream().map(this::mapToOrderItems)
                .toList();

        order.setOrderItemsList(orderItems);

        orderRepository.save(order);
    }

    private OrderItems mapToOrderItems(OrderItemsDto orderItemsDto) {
        var orderItems = new OrderItems();
        orderItems.setPrice(orderItemsDto.getPrice());
        orderItems.setQuantity(orderItemsDto.getQuantity());
        orderItems.setSkuCode(orderItemsDto.getSkuCode());

        return orderItems;
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToOrderResponse);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderNumber(),
                order.getOrderItemsList());
    }
}
