package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.dao.operations.IngredientDAO;
import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.models.dish.DishIngredient;
import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {
    private final IngredientDAO ingredientDAO;

    @SneakyThrows
    @Override
    public DishIngredient apply(ResultSet resultSet) {
        int idDish = resultSet.getInt("id_dish");
        int idIngredient = resultSet.getInt("id_ingredient");
        Double requiredQuantity = resultSet.getDouble("required_quantity");
        Unit unit = Unit.valueOf(resultSet.getString("unit"));

        Ingredient ingredient;
        try {
            ingredient = ingredientDAO.getById(idIngredient);
        } catch (NotFoundException e) {
            ingredient = null;
        }

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIdDish(idDish);
        dishIngredient.setIdIngredient(idIngredient);
        dishIngredient.setUnit(unit);
        dishIngredient.setRequireQuantity(requiredQuantity);
        dishIngredient.setIngredient(ingredient);

        return dishIngredient;
    }
}
