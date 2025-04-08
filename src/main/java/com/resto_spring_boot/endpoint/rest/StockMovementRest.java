package com.resto_spring_boot.endpoint.rest;

import com.resto_spring_boot.models.Stock.Movement;
import com.resto_spring_boot.models.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private int IdMovement;
    private Double quantity;
    private Unit unit;
    private Movement movementType;
    private LocalDateTime movementDateTime;
}
