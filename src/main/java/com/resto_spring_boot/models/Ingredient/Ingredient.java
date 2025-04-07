package com.resto_spring_boot.models.Ingredient;

import com.resto_spring_boot.models.Stock.StockMovement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ingredient {
    private int idIngredient;
    private String ingredientName;
    private List<IngredientPriceHistory> prices;
    private List<StockMovement> stockMovements;

}
