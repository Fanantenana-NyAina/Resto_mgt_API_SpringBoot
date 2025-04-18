package com.resto_spring_boot.models.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishOrderStatusHistory {
    private OrderStatus dishOrderStatus;
    private LocalDateTime dishOrderDateTime;
}
