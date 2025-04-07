package com.resto_spring_boot.dao.mapper;


import com.resto_spring_boot.dao.operations.IngredientPriceHistoryDAO;
import com.resto_spring_boot.dao.operations.StockMovementDAO;
import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.models.Ingredient.IngredientPriceHistory;
import com.resto_spring_boot.models.Stock.StockMovement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {
    private IngredientPriceHistoryDAO ingredientPriceHistoryDAO;
    private StockMovementDAO stockMovementDAO;

    @SneakyThrows
    @Override
    public Ingredient apply(ResultSet resultSet) {
        int idIngredient = resultSet.getInt("id_ingredient");
        List<IngredientPriceHistory> ingredientPrices = ingredientPriceHistoryDAO.findByIdIngredient(idIngredient);
        List<StockMovement> ingredientStockMovements = stockMovementDAO.findByIdIngredient(idIngredient);

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient(idIngredient);
        ingredient.setIngredientName(resultSet.getString("name"));
        ingredient.setPrices(ingredientPrices);
        ingredient.setStockMovements(ingredientStockMovements);
        return ingredient;
    }
}
