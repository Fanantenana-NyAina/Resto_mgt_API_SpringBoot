package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.dao.operations.DishIngredientDAO;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.models.dish.DishIngredient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
    private DishIngredientDAO dishIngredientDAO;

    @SneakyThrows
    @Override
    public Dish apply(ResultSet resultSet) {
        int idDish = resultSet.getInt("id_dish");
        List<DishIngredient> dishIngredientsList = dishIngredientDAO.findDishIngredientByIdDish(idDish);

        Dish dish = new Dish();
        dish.setIdDish(idDish);
        dish.setDishName(resultSet.getString("name"));
        dish.setUnitPrice(resultSet.getDouble("unit_price"));
        dish.setDishIngredients(dishIngredientsList);

        return dish;
    }
}
