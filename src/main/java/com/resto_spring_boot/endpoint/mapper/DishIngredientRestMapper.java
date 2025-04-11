package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.DishIngredientRest;
import com.resto_spring_boot.models.dish.DishIngredient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class DishIngredientRestMapper implements Function<DishIngredient, DishIngredientRest> {
    @Override
    public DishIngredientRest apply(DishIngredient dishIngredient) {
        if (dishIngredient == null) {
            return null;
        }

        return new DishIngredientRest(
                dishIngredient.getRequireQuantity(),
                dishIngredient.getUnit(),
                dishIngredient.getIngredient().getIngredientName()
        );
    }
}
