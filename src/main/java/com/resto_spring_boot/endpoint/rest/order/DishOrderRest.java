package com.resto_spring_boot.endpoint.rest.order;

import com.resto_spring_boot.models.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishOrderRest {
    private String dishName;
    private Double currentPrice;
    private Double quantity;
    private OrderStatus dishActualStatus;
}
