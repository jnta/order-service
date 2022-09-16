package com.jonata.orderservice.service;

import com.jonata.orderservice.dto.InventoryResponse;
import com.jonata.orderservice.dto.OrderItemDto;
import com.jonata.orderservice.dto.OrderRequest;
import com.jonata.orderservice.dto.OrderResponse;
import com.jonata.orderservice.model.Order;
import com.jonata.orderservice.model.OrderItems;
import com.jonata.orderservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItems = orderRequest.getItems()
                .stream().map(itemDto -> this.mapToOrderItems(itemDto, order))
                .toList();

        order.setOrderItems(orderItems);

        InventoryResponse[] inventoryResponseArray = getProductsInInvetory(orderItems);

        boolean allProductsInStock = isAllProductsInStock(inventoryResponseArray);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            System.out.println("Product not in stock");
        }

    }

    private InventoryResponse[] getProductsInInvetory(List<OrderItems> orderItems) {
        List<String> skuCodes = orderItems.stream().map(OrderItems::getSkuCode).toList();

        return webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
    }

    private boolean isAllProductsInStock(InventoryResponse[] inventoryResponseArray) {
        if (inventoryResponseArray.length == 0) {
            return false;
        }

        for (InventoryResponse inventoryResponse : inventoryResponseArray) {
            if (inventoryResponse.isInStock() == false) {
                return false;
            }
        }
        return true;
    }

    private OrderItems mapToOrderItems(OrderItemDto orderItemDto, Order order) {
        var orderItems = new OrderItems();
        orderItems.setPrice(orderItemDto.getPrice());
        orderItems.setQuantity(orderItemDto.getQuantity());
        orderItems.setSkuCode(orderItemDto.getSkuCode());
        orderItems.setOrder(order);

        return orderItems;
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::new);
    }

}
