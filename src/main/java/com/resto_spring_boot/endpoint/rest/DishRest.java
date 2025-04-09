package com.resto_spring_boot.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DishRest {
    private int idDish;
    private String dishName;
    private Double unitPrice;
    private List<DishIngredientRest> dishIngredients;
}
