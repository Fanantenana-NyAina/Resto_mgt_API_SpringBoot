package com.resto_spring_boot.models.Ingredient;

import com.resto_spring_boot.models.Stock.StockMovement;
import com.resto_spring_boot.models.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ingredient {
    private int idIngredient;
    private String ingredientName;
    private Unit unit;
    private List<IngredientPriceHistory> prices;
    private List<StockMovement> stockMovements;

}
