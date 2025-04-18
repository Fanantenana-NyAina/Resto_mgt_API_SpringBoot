package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.order.DishOrderRest;
import com.resto_spring_boot.endpoint.rest.order.OrderRest;
import com.resto_spring_boot.models.order.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderRestMapper {
    private DishOrderRestMapper dishOrderRestMapper;

    public OrderRest orderToOrderRest (Order order) {
        List<DishOrderRest> dishOrders = order.getDishOrders().stream()
                .map(dishOrderRestMapper::apply).toList();

        OrderRest orderRest = new OrderRest();
        orderRest.setOrderRef(order.getOrderIdRef());
        orderRest.setTotalCost(order.getTotalAmount());
        orderRest.setOrderStatus(order.getActualOrderStatus());
        orderRest.setDishInOrder(dishOrders);
        return orderRest;
    }
}
