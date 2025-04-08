package com.resto_spring_boot.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PricingIngredient {
    private Double price;
    private LocalDateTime dateTime;
}
