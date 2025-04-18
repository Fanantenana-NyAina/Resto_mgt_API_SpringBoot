package com.resto_spring_boot.endpoint.rest.order;

import com.resto_spring_boot.models.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRest {
    private int orderRef;
    private Double totalCost;
    private OrderStatus orderStatus;
    private List<DishOrderRest> dishInOrder;
}
