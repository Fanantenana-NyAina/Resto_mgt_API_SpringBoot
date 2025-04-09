package com.resto_spring_boot.models.stock;

import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.models.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockMovement {
    private int IdMovement;
    private Ingredient Ingredient;
    private Double quantity;
    private Unit unit;
    private Movement movementType;
    private LocalDateTime movementDateTime;
}
