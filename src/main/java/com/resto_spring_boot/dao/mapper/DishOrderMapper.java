package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.dao.operations.DishDAO;
import com.resto_spring_boot.dao.operations.order.DishOrderDAO;
import com.resto_spring_boot.dao.operations.order.DishOrderStatusHistoryDAO;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.models.order.DishOrder;
import com.resto_spring_boot.models.order.DishOrderStatusHistory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class DishOrderMapper implements Function<ResultSet, DishOrder> {
    private final DishOrderStatusHistoryDAO dishOrderStatusHistoryDAO;
    private final DishDAO dishDAO;

    @SneakyThrows
    @Override
    public DishOrder apply(ResultSet resultSet) {
        Dish dish = dishDAO.getById(resultSet.getInt("id_dish"));
        List<DishOrderStatusHistory> dishOrderStatusHistories = dishOrderStatusHistoryDAO.findDishOrderStatusByDishAndOrder(resultSet.getInt("id_dish"),resultSet.getInt("id_order_as_reference"));

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setQuantity(resultSet.getDouble("quantity"));
        dishOrder.setDishOrderStatusHistories(dishOrderStatusHistories);
        return dishOrder;
    }
}
