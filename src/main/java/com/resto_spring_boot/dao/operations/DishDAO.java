package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishMapper;
import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.models.dish.DishIngredient;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishDAO implements DAO<Dish> {
    private final DbConnection dataSource;
    private final DishMapper dishMapper;

    @Override
    public List<Dish> getAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "select  d.id_dish, d.name, d.unit_price from dish d order by d.id_dish asc limit ? offset ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, size);
            ps.setInt(2, (page -1)*size);

            if (con == null || con.isClosed()) {
                throw new SQLException("The connection is closed or invalid.");
            }

            try (ResultSet rs = ps.executeQuery()) {
                Dish dish = null;
                while (rs.next()) {
                    dish = dishMapper.apply(rs);
                    dishes.add(dish);
                }

                return dishes;
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Dish getById(int id) {
        String sql = "select d.id_dish, d.name, d.unit_price from dish d where d.id_dish = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = dishMapper.apply(rs);
                    List<DishIngredient> ingredients = getDishIngredients(id);
                    dish.setDishIngredients(ingredients);

                    return dish;
                } else {
                    throw new NotFoundException("Dish with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public List<Dish> saveAll(List<Dish> entities) {
        List<Dish> savedDishes = new ArrayList<>();
        String sql = "insert into dish (name, unit_price) values (?, ?) "
                + "on conflict (id_dish) do update set name=excluded.name, unit_price=excluded.unit_price "
                + "returning id_dish, name, unit_price";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                for (Dish dish : entities) {
                    ps.setString(1, dish.getDishName());
                    ps.setDouble(2, dish.getUnitPrice());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Dish savedDish = dishMapper.apply(rs);
                            savedDishes.add(savedDish);
                        }
                    }
                }

                con.commit();
                return savedDishes;

            } catch (SQLException e) {
                con.rollback();
                throw new ServerException(e);
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    private List<DishIngredient> getDishIngredients(int dishId) throws SQLException {
        String sql = "select di.id_ingredient, di.required_quantity, di.unit, i.name as ingredient_name " +
                "from dish_ingredient di " +
                "join ingredient i on di.id_ingredient = i.id_ingredient " +
                "where di.id_dish = ?";

        List<DishIngredient> ingredients = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dishId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DishIngredient di = new DishIngredient();
                    di.setIdDish(dishId);
                    di.setIdIngredient(rs.getInt("id_ingredient"));
                    di.setRequireQuantity(rs.getDouble("required_quantity"));
                    di.setUnit(Unit.valueOf(rs.getString("unit")));
                    di.setIngredientName(rs.getString("ingredient_name"));
                    ingredients.add(di);
                }
            }
        }
        return ingredients;
    }
}
