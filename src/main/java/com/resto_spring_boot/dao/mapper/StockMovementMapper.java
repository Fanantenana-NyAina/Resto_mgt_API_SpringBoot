package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.Stock.Movement;
import com.resto_spring_boot.models.Stock.StockMovement;
import com.resto_spring_boot.models.Unit;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class StockMovementMapper implements Function<ResultSet, StockMovement> {
    @SneakyThrows
    @Override
    public StockMovement apply(ResultSet resultSet) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setIdMovement(resultSet.getInt("idMovement"));
        stockMovement.setMovementType(Movement.valueOf(resultSet.getString("movementType")));
        stockMovement.setQuantity(resultSet.getDouble("quantity"));
        stockMovement.setUnit(Unit.valueOf(resultSet.getString("unit")));
        stockMovement.setMovementDateTime(resultSet.getObject("movementDateTime", LocalDateTime.class));

        return stockMovement;
    }
}
