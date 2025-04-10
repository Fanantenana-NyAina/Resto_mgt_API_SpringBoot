package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.DishIngredientRest;
import com.resto_spring_boot.endpoint.rest.DishRest;
import com.resto_spring_boot.models.dish.Dish;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DishRestMapper {
    private final DishIngredientRestMapper dishIngredientRestMapper;

    public DishRest DishToDishRest(Dish dish) {
        List<DishIngredientRest> dishIngredients = dish.getDishIngredients().stream()
                .map(dishIngredient -> dishIngredientRestMapper.apply(dishIngredient))
                .toList();

        return new DishRest(
                dish.getIdDish(),
                dish.getDishName(),
                dish.getUnitPrice(),
                dish.getAvalaibleQuantity(),
                dishIngredients
        );
    }
}
