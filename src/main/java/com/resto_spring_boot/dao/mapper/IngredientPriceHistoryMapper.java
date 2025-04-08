package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.Ingredient.IngredientPriceHistory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class IngredientPriceHistoryMapper implements Function<ResultSet, IngredientPriceHistory> {
    @SneakyThrows
    @Override
    public IngredientPriceHistory apply(ResultSet resultSet) {
        IngredientPriceHistory ingredientPriceHistory = new IngredientPriceHistory();
        ingredientPriceHistory.setIdPriceHistory(resultSet.getInt("id_price_history"));
        ingredientPriceHistory.setPrice(resultSet.getDouble("price"));
        ingredientPriceHistory.setDateTime(resultSet.getObject("history_date", LocalDateTime.class));

        return ingredientPriceHistory;
    }
}
