package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.dao.operations.IngredientDAO;
import com.resto_spring_boot.endpoint.rest.CreateOrUpdateIngredient;
import com.resto_spring_boot.endpoint.rest.IngredientPriceHistoryRest;
import com.resto_spring_boot.endpoint.rest.IngredientRest;
import com.resto_spring_boot.endpoint.rest.StockMovementRest;
import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class IngredientRestMapper {
    private final IngredientPriceHistoryRestMapper ingredientPriceHistoryRestMapper;
    private final StockMovementRestMapper stockMovementRestMapper;
    private final IngredientDAO ingredientDAO;

    public IngredientRest IngredientToIngredientRest(Ingredient ingredient) {
        List<IngredientPriceHistoryRest> ingredientPrices = ingredient.getPrices().stream()
                .map(ingredientPrice -> ingredientPriceHistoryRestMapper.apply((ingredientPrice)))
                .toList();
        List<StockMovementRest> stockMovementRests = ingredient.getStockMovements().stream()
                .map(stockMovement -> stockMovementRestMapper.apply(stockMovement))
                .toList();

        return new IngredientRest(ingredient.getIdIngredient(), ingredient.getIngredientName(), ingredientPrices, stockMovementRests);
    }

    public Ingredient APIingredientToModelIngredient(CreateOrUpdateIngredient newIngredient) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient(newIngredient.getIdIngredient());
        ingredient.setIngredientName(newIngredient.getIngredientName());

        try {
            Ingredient checkExistingIngredient = ingredientDAO.getById(newIngredient.getIdIngredient());
            ingredient.addPrices(checkExistingIngredient.getPrices());
            ingredient.addStockMovements(checkExistingIngredient.getStockMovements());
        } catch (NotFoundException e) {
            ingredient.addPrices(new ArrayList<>());
            ingredient.addStockMovements(new ArrayList<>());
        }

        return ingredient;
    }
}
