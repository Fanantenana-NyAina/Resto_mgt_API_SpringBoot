package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.mapper.IngredientMapper;
import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientDAO implements DAO<Ingredient> {
    private final DbConnection dataSource;
    private final IngredientMapper ingredientMapper;
    private final IngredientPriceHistoryDAO ingredientPriceHistoryDAO;
    private final StockMovementDAO stockMovementDAO;

    @Override
    public List<Ingredient> getAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "select i.id_ingredient, i.name from ingredient i order by i.id_ingredient asc limit ? offset ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);){
            ps.setInt(1, size);
            ps.setInt(2, (page -1)*size);

            try (ResultSet rs = ps.executeQuery()) {
                Ingredient ingredient = null;
                while (rs.next()) {
                    ingredient = ingredientMapper.apply(rs);
                    ingredients.add(ingredient);
                }

                return ingredients;
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Ingredient getById(int id) {
        String sql = "select i.id_ingredient, i.name, di.required_quantity, di.unit from ingredient i"
                + " join dish_ingredient di on i.id_ingredient = di.id_ingredient"
                + " where i.id_ingredient = ?";

        try ( Connection con = dataSource.getConnection();
              PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return ingredientMapper.apply(rs);
                }

                throw new NotFoundException("Ingredient.id_ingredient =" + id + " not found");
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        List<Ingredient> newIngredients = new ArrayList<>();
        String sql = "insert into ingredient (id_ingredient, name) values (?, ?)"
                + " on conflict (id_ingredient) do update set name=excluded.name"
                + " returning id_ingredient, name";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                for (Ingredient entityToSave : entities) {
                    ps.setInt(1, entityToSave.getIdIngredient());
                    ps.setString(2, entityToSave.getIngredientName());
                    ps.addBatch();

                    ingredientPriceHistoryDAO.saveAll(entityToSave.getPrices());
                    stockMovementDAO.saveAll(entityToSave.getStockMovements());
                }
                ps.executeBatch();

                try(ResultSet rs = ps.getGeneratedKeys()) {
                    while (rs.next()) {
                        newIngredients.add(ingredientMapper.apply(rs));
                    }
                }

                con.commit();
                return newIngredients;

            } catch (SQLException e) {
                con.rollback();
                throw new ServerException(e);
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
