package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.mapper.StockMovementMapper;
import com.resto_spring_boot.models.Stock.StockMovement;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class StockMovementDAO implements DAO<StockMovement> {
    private DataSource dataSource;
    private StockMovementMapper stockMovementMapper;

    @Override
    public List<StockMovement> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StockMovement getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StockMovement> saveAll(List<StockMovement> entities) {
        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = """
                insert into stock_movement (id_movement, id_ingredient, movement, quantity, unit, movement_datetime)
                values (?, ?, ?, ?, ?, ?)
                on conflict (id_movement) do nothing returning id_movement, id_ingredient, movement, quantity, unit, movement_datetime""";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            entities.forEach(entityToSave -> {
                try {
                    ps.setInt(1, entityToSave.getIdMovement());
                    ps.setInt(2, entityToSave.getIngredient().getIdIngredient());
                    ps.setObject(3, entityToSave.getMovementType());
                    ps.setDouble(5, entityToSave.getQuantity());
                    ps.setObject(4, entityToSave.getUnit());
                    ps.setObject(6, entityToSave.getMovementDateTime());
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stockMovements.add(stockMovementMapper.apply(rs));
                }
            }
            return stockMovements;
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public List<StockMovement> findByIdIngredient(int idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id_movement, s.movement, s.quantity, s.unit, s.movement_datetime from stock_movement s"
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
