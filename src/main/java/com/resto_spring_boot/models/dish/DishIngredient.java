package com.resto_spring_boot.models.dish;

import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.models.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishIngredient {
    private int idDish;
    private int idIngredient;
    private Double requireQuantity;
    private Unit unit;
    private Ingredient ingredient;
}
