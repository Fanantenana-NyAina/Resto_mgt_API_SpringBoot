package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.order.DishOrderStatusHistory;
import com.resto_spring_boot.models.order.OrderStatus;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class DishOrderStatusHistoryMapper implements Function<ResultSet, DishOrderStatusHistory> {

    @SneakyThrows
    @Override
    public DishOrderStatusHistory apply(ResultSet resultSet) {
        DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory();
        dishOrderStatusHistory.setDishOrderStatus(OrderStatus.valueOf(resultSet.getString("dish_order_status")));
        dishOrderStatusHistory.setDishOrderDateTime(resultSet.getTimestamp("status_datetime").toLocalDateTime());

        return dishOrderStatusHistory;
    }
}
