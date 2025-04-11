package com.resto_spring_boot.endpoint.rest;

import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.models.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDishIngredients {
    private Ingredient ingredient;
    private Double requiredQuantity;
    private Unit unit;
}
