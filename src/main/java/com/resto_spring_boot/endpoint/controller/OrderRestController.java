package com.resto_spring_boot.endpoint.controller;

import com.resto_spring_boot.endpoint.mapper.OrderRestMapper;
import com.resto_spring_boot.endpoint.rest.order.OrderRest;
import com.resto_spring_boot.models.order.Order;
import com.resto_spring_boot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;
    private final OrderRestMapper orderRestMapper;

    @GetMapping("/orders/{orderRef}")
    public ResponseEntity<OrderRest> getOrder(@PathVariable int orderRef){
        Order order = orderService.getOrderByReference(orderRef);
        OrderRest orderRest = orderRestMapper.orderToOrderRest(order);

        return ResponseEntity.status(HttpStatus.OK).body(orderRest);
    }

    @PutMapping("/orders/{orderRef}/dishes")
    public ResponseEntity<Object> addDishOrder () {
        throw new UnsupportedOperationException("not supported yet");
    }

    @GetMapping("/orders/{orderRef}/dishes/{dishId}")
    public ResponseEntity<Object> changeDishOrderStateByIdDish () {
        throw new UnsupportedOperationException("not supported yet");
    }
}
