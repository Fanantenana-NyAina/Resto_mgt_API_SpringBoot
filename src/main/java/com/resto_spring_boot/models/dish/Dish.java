package com.resto_spring_boot.models.dish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Dish {
    private int idDish;
    private String dishName;
    private Double unitPrice;
    private List<DishIngredient> dishIngredients;

    public int getAvalaibleQuantity() {
        if (dishIngredients == null || dishIngredients.isEmpty()) {
            return 0;
        }

        return dishIngredients.stream()
                .mapToInt(di -> {
                    double inStock = di.getIngredient().getAvailableQuantity();
                    double required = di.getRequireQuantity();
                    if (required <= 0) return 0;
                    return (int) (inStock / required);
                })
                .min()
                .orElse(0);
    }
}
