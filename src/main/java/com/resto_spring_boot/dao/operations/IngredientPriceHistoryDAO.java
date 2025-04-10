package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.IngredientPriceHistoryMapper;
import com.resto_spring_boot.models.ingredient.IngredientPriceHistory;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientPriceHistoryDAO implements DAO<IngredientPriceHistory> {
    private final DbConnection dataSource;
    private final IngredientPriceHistoryMapper ingredientPriceHistoryMapper;

    @Override
    public List<IngredientPriceHistory> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IngredientPriceHistory getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<IngredientPriceHistory> saveAll(List<IngredientPriceHistory> entities) {
        List<IngredientPriceHistory> savedPrices = new ArrayList<>();

        String sql = "insert into ingredient_price_history (id_ingredient, price, history_date) " +
                "values (?, ?, ?) returning id_price_history, id_ingredient, price, history_date";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (IngredientPriceHistory entity : entities) {
                    ps.setInt(1, entity.getIngredient().getIdIngredient());
                    ps.setDouble(2, entity.getIngredientPrice());
                    ps.setObject(3, entity.getDateTime());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            savedPrices.add(ingredientPriceHistoryMapper.apply(rs));
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new ServerException(e);
            } finally {
                con.setAutoCommit(true);
            }
        }
        return savedPrices;
    }

    public List<IngredientPriceHistory> findByIdIngredient(int idIngredient) {
        String sql = "select iph.id_price_history, iph.price, iph.history_date, iph.id_ingredient from ingredient_price_history iph"
                + " join ingredient i on iph.id_ingredient = i.id_ingredient"
                + " where iph.id_ingredient = ?";
        List<IngredientPriceHistory> prices = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idIngredient);
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
