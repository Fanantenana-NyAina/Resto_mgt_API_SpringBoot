package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.dao.operations.order.DishOrderDAO;
import com.resto_spring_boot.dao.operations.order.OrderStatusHistoryDAO;
import com.resto_spring_boot.models.order.DishOrder;
import com.resto_spring_boot.models.order.Order;
import com.resto_spring_boot.models.order.OrderStatusHistory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {
    private final DishOrderDAO dishOrderDAO;
    private final OrderStatusHistoryDAO orderStatusHistoryDAO;

    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        int orderRef = resultSet.getInt("id_order_as_reference");
        List<OrderStatusHistory> orderStatusHistories = orderStatusHistoryDAO.findByOrderRef(orderRef);
        List<DishOrder> dishOrders = dishOrderDAO.findByOrderRef(orderRef);

        Order order = new Order();
        order.setOrderIdRef(orderRef);
        order.setOrderStatus(orderStatusHistories);
        order.setDishOrders(dishOrders);

        return order;
    }
}
