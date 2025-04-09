package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.IngredientPriceHistoryRest;
import com.resto_spring_boot.models.ingredient.IngredientPriceHistory;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IngredientPriceHistoryRestMapper implements Function<IngredientPriceHistory, IngredientPriceHistoryRest> {

    @Override
    public IngredientPriceHistoryRest apply(IngredientPriceHistory ingredientPriceHistory) {
        if (ingredientPriceHistory == null) {
            return null;
        }

        return new IngredientPriceHistoryRest(
                ingredientPriceHistory.getIdPriceHistory(),
                ingredientPriceHistory.getPrice(),
                ingredientPriceHistory.getDateTime()
        );
    }
}
