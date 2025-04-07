package com.resto_spring_boot.models.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IngredientPriceHistory {
    private int idPriceHistory;
    private Ingredient ingredient;
    private double price;
    private LocalDateTime dateTime;
}
