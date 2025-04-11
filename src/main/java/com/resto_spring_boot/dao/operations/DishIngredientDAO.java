package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishIngredientMapper;
import com.resto_spring_boot.models.dish.DishIngredient;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishIngredientDAO implements DAO<DishIngredient> {
    private final DbConnection datasource;
    private final DishIngredientMapper dishIngredientMapper;

    @Override
    public List<DishIngredient> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DishIngredient getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> entities) {
        List<DishIngredient> savedDishIngredients = new ArrayList<>();

        String sql = """
        insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit)
        values (?, ?, ?, ?::unit)
        on conflict (id_dish, id_ingredient)
        do update set required_quantity = excluded.required_quantity, unit = excluded.unit
        returning id_dish, id_ingredient, required_quantity, unit
    """;

        try (Connection con = datasource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (DishIngredient entity : entities) {
                    ps.setInt(1, entity.getIdDish());
                    ps.setInt(2, entity.getIngredient().getIdIngredient());
                    ps.setDouble(3, entity.getRequireQuantity());
                    ps.setString(4, entity.getUnit().toString());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            savedDishIngredients.add(dishIngredientMapper.apply(rs));
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

        return savedDishIngredients;
    }


    public List<DishIngredient> findDishIngredientByIdDish(int idDish) {
        String sql = "select di.id_dish, di.id_ingredient, di.required_quantity, di.unit " +
                "from dish_ingredient di join ingredient i on di.id_ingredient = i.id_ingredient where di.id_dish = ?";
        List<DishIngredient> dishIngredients = new ArrayList<>();

        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDish);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishIngredient dishIngredient = dishIngredientMapper.apply(resultSet);
                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @SneakyThrows
    public List<DishIngredient> saveAllDishIngredient(List<DishIngredient> items, int idDish) {

        List<DishIngredient> dishIngredients = new ArrayList<>();
        String sql = """
       insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit)
        values (?, ?, ?, ?::unit)
        on conflict (id_dish, id_ingredient)
        do update set required_quantity = excluded.required_quantity, unit = excluded.unit
        returning id_dish, id_ingredient, required_quantity, unit
        """;

        try (Connection conn = datasource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (DishIngredient item : items) {
                ps.setInt(1, idDish);
                ps.setInt(2, item.getIngredient().getIdIngredient());
                ps.setDouble(3, item.getRequireQuantity());
                ps.setString(4, item.getUnit().toString());
                ps.addBatch();
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dishIngredients.add(dishIngredientMapper.apply(rs));
                }
            }
            return dishIngredients;
        }
    }
}
