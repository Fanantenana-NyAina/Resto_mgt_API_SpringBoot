package com.resto_spring_boot.models.order;

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
public class Order {
    private int orderIdRef;
    private List<OrderStatusHistory> orderStatus;
    private List<DishOrder> dishOrders;

    public OrderStatus getActualOrderStatus() {
        return orderStatus.stream()
                .max(Comparator.comparing(OrderStatusHistory::getOrderDatetime))
                .map(OrderStatusHistory::getStatus)
                .orElse(OrderStatus.CREATED);
    }

    public double getTotalAmount(){
        return dishOrders.stream()
                .map(d->d.getQuantity() * d.getDish().getUnitPrice())
                .reduce(0.0, Double::sum);
    }
}
