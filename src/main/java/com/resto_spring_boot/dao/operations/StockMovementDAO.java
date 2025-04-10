package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.StockMovementMapper;
import com.resto_spring_boot.models.stock.StockMovement;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockMovementDAO implements DAO<StockMovement> {
    private final DbConnection dataSource;
    private final StockMovementMapper stockMovementMapper;

    @Override
    public List<StockMovement> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StockMovement getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<StockMovement> saveAll(List<StockMovement> entities) {
        List<StockMovement> savedMovements = new ArrayList<>();

        String sql = "insert into stock_movement (id_ingredient, movement, quantity, unit) " +
                "values (?, ?::mvt, ?, ?::unit) " +
                "returning id_movement, id_ingredient, movement, quantity, unit, movement_datetime";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (StockMovement entity : entities) {
                    ps.setInt(1, entity.getIngredient().getIdIngredient());
                    ps.setString(2, entity.getMovementType().toString());
                    ps.setDouble(3, entity.getQuantity());
                    ps.setString(4, entity.getUnit().toString());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            savedMovements.add(stockMovementMapper.apply(rs));
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

        return savedMovements;
    }

    public List<StockMovement> findByIdIngredient(int idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id_movement, s.movement, s.quantity, s.unit, s.movement_datetime, s.id_ingredient from stock_movement s"
                             + " join ingredient i on s.id_ingredient = i.id_ingredient"
                             + " where s.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
