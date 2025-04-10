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
    private Ingredient ingredient;
    private Double quantity;
    private Unit unit;
    private Movement movementType;
    private LocalDateTime movementDateTime;

    public StockMovement(Ingredient ingredient, Double quantity, Unit unit, Movement movementType, LocalDateTime movementDateTime) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.movementType = movementType;
        this.movementDateTime = movementDateTime;
    }

    public StockMovement(Double quantity, Unit unit, Movement movementType, LocalDateTime movementDateTime) {
        this.quantity = quantity;
        this.unit = unit;
        this.movementType = movementType;
        this.movementDateTime = movementDateTime;
    }
}
