package com.resto_spring_boot.models.order;

import com.resto_spring_boot.models.dish.Dish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishOrder {
    private Dish dish;
    private Double quantity;
    private List<DishOrderStatusHistory> dishOrderStatusHistories;

    public OrderStatus getActualDishOrderStatus() {
        return dishOrderStatusHistories.stream()
                .max(Comparator.comparing(DishOrderStatusHistory::getDishOrderDateTime))
                .map(DishOrderStatusHistory::getDishOrderStatus).orElse(OrderStatus.CREATED);
    }
}
