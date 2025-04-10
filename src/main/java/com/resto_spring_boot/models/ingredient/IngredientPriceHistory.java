package com.resto_spring_boot.models.ingredient;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IngredientPriceHistory {
    private int idPriceHistory;
    private Ingredient ingredient;
    private Double ingredientPrice;
    private LocalDateTime dateTime;

    public IngredientPriceHistory(Double ingredientPrice, LocalDateTime dateTime) {
        this.ingredientPrice = ingredientPrice;
        this.dateTime = dateTime;
    }

    public IngredientPriceHistory(Ingredient ingredient, Double ingredientPrice, LocalDateTime dateTime) {
        this.ingredient = ingredient;
        this.ingredientPrice = ingredientPrice;
        this.dateTime = dateTime;
    }
}
