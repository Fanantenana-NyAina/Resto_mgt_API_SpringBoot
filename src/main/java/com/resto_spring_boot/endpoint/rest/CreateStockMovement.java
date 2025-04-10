package com.resto_spring_boot.endpoint.rest;

import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.models.stock.Movement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateStockMovement {
    private Double quantity;
    private Unit unit;
    private Movement movementType;
    private LocalDateTime movementDateTime;
}
