package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.order.DishOrderRest;
import com.resto_spring_boot.models.order.DishOrder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {

    @Override
    public DishOrderRest apply(DishOrder dishOrder) {
        if (dishOrder == null) {
            return null;
        }

        DishOrderRest dishOrderRest = new DishOrderRest();
        dishOrderRest.setDishName(dishOrder.getDish().getDishName());
        dishOrderRest.setQuantity(dishOrder.getQuantity());
        dishOrderRest.setDishActualStatus(dishOrder.getActualDishOrderStatus());

        return dishOrderRest;
    }
}
