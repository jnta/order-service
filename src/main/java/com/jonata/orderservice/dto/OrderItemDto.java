package com.jonata.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
