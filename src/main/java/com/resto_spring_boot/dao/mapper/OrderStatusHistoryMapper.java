package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.order.OrderStatus;
import com.resto_spring_boot.models.order.OrderStatusHistory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class OrderStatusHistoryMapper implements Function<ResultSet, OrderStatusHistory> {

    @SneakyThrows
    @Override
    public OrderStatusHistory apply(ResultSet resultSet) {
        OrderStatusHistory orderStatus = new OrderStatusHistory();
        orderStatus.setStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
        orderStatus.setOrderDatetime(resultSet.getTimestamp("status_datetime").toLocalDateTime());

        return orderStatus;
    }
}
