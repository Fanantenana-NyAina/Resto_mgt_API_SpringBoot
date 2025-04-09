package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishIngredientMapper;
import com.resto_spring_boot.models.dish.DishIngredient;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> entity) {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
