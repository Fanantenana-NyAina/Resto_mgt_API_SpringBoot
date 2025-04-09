package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishMapper;
import com.resto_spring_boot.models.dish.Dish;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Dish> saveAll(List<Dish> entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
