package com.resto_spring_boot.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
    private int idIngredient;
    private String ingredientName;
    private List<IngredientPriceHistoryRest> prices;
    private List<StockMovementRest> stockMovements;
}
