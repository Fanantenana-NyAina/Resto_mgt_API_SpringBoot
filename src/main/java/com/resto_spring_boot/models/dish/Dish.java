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
}
