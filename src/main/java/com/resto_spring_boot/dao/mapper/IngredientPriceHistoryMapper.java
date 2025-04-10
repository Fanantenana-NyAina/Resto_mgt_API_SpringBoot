package com.resto_spring_boot.dao.mapper;

import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.models.ingredient.IngredientPriceHistory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class IngredientPriceHistoryMapper implements Function<ResultSet, IngredientPriceHistory> {
    @SneakyThrows
    @Override
    public IngredientPriceHistory apply(ResultSet rs) {
        // Crée un nouvel historique
        IngredientPriceHistory history = new IngredientPriceHistory();

        // Remplit les champs de base
        history.setIdPriceHistory(rs.getInt("id_price_history"));
        history.setIngredientPrice(rs.getDouble("price"));
        history.setDateTime(rs.getObject("history_date", LocalDateTime.class));

        // Crée et associe l'ingrédient (CRUCIAL)
        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient(rs.getInt("id_ingredient"));
        history.setIngredient(ingredient);

        return history;
    }
}
