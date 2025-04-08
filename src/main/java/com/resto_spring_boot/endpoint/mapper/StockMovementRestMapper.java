package com.resto_spring_boot.endpoint.mapper;

import com.resto_spring_boot.endpoint.rest.StockMovementRest;
import com.resto_spring_boot.models.Stock.StockMovement;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {

    @Override
    public StockMovementRest apply(StockMovement stockMovement) {
        return new StockMovementRest(
                stockMovement.getIdMovement(),
                stockMovement.getQuantity(),
                stockMovement.getUnit(),
                stockMovement.getMovementType(),
                stockMovement.getMovementDateTime()
        );
    }
}
