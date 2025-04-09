package com.resto_spring_boot.endpoint.rest;

import com.resto_spring_boot.models.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishIngredientRest {
    private int idDish;
    private int idIngredient;
    private Double requireQuantity;
    private Unit unit;
    private String ingredientName;
}
