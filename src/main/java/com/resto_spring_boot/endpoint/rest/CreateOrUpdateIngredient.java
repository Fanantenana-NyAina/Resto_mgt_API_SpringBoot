package com.resto_spring_boot.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrUpdateIngredient {
    private int idIngredient;
    private String ingredientName;
}
