package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.Ingredient.IngredientPriceHistory;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.function.Function;

public class IngredientPriceHistoryMapper implements Function<ResultSet, IngredientPriceHistory> {
    @SneakyThrows
    @Override
    public IngredientPriceHistory apply(ResultSet resultSet) {
        IngredientPriceHistory ingredientPriceHistory = new IngredientPriceHistory();
        ingredientPriceHistory.setIdPriceHistory(resultSet.getInt("idPriceHistory"));
        ingredientPriceHistory.setPrice(resultSet.getDouble("price"));
        ingredientPriceHistory.setDateTime(LocalDateTime.parse(resultSet.getString("history_date")));
        return null;
    }
}
