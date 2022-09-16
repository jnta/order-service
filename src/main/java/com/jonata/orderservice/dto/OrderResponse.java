package com.jonata.orderservice.dto;

import com.jonata.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private List<OrderItemDto> orderItemsDto = new ArrayList<>();

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderItemsDto = order.getOrderItems().stream()
                .map(item ->
                        new OrderItemDto(
                                item.getId(),
                                item.getSkuCode(),
                                item.getPrice(),
                                item.getQuantity()))
                .toList();
    }
}
