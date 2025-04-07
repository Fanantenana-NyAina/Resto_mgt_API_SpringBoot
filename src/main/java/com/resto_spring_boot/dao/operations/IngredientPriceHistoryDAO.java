package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.mapper.IngredientPriceHistoryMapper;
import com.resto_spring_boot.models.Ingredient.IngredientPriceHistory;
import com.resto_spring_boot.models.Stock.StockMovement;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientPriceHistoryDAO implements DAO<IngredientPriceHistory> {
    private DataSource dataSource;
    private IngredientPriceHistoryMapper ingredientPriceHistoryMapper;

    @Override
    public List<IngredientPriceHistory> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IngredientPriceHistory getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<IngredientPriceHistory> saveAll(List<IngredientPriceHistory> entities) {
        List<IngredientPriceHistory> prices = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "insert into price (id_price_history, id_ingredient, price, history_date) values (?, ?, ?, ?)"
                             + " on conflict (id_price_history) do nothing"
                             + " id_price_history, id_ingredient, price, history_date")) {
            entities.forEach(entityToSave -> {
                try {
                    statement.setInt(1, entityToSave.getIdPriceHistory());
                    statement.setInt(2, entityToSave.getIngredient().getIdIngredient());
                    statement.setDouble(3, entityToSave.getPrice());
                    statement.setObject(4, entityToSave.getDateTime());
                    statement.addBatch();
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    prices.add(ingredientPriceHistoryMapper.apply(rs));
                }
            }

            return prices;
        }
    }

    public List<IngredientPriceHistory> findByIdIngredient(int idIngredient) {
        List<IngredientPriceHistory> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id_price_history, p.price, p.history_date from ingredient_price_history p"
                     + " join ingredient i on p.id_ingredient = i.id_ingredient"
                     + " where p.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    IngredientPriceHistory price = ingredientPriceHistoryMapper.apply(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
