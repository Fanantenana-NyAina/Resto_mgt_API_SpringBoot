package com.resto_spring_boot.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class IngredientPriceHistoryRest {
    private int idPriceHistory;
    private double price;
    private LocalDateTime dateTime;
}
