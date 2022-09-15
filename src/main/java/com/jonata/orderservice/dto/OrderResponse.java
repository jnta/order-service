package com.jonata.orderservice.dto;

import com.jonata.orderservice.model.OrderItems;
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
    private List<OrderItems> orderItemsList = new ArrayList<>();
}
