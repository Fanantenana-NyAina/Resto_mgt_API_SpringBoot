package com.resto_spring_boot.dao.operations.order;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.OrderMapper;
import com.resto_spring_boot.dao.operations.DAO;
import com.resto_spring_boot.models.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDAO implements DAO<Order> {
    private final DbConnection dataSource;
    private final OrderMapper orderMapper;

    @Override
    public List<Order> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public Order getById(int id) {
        return null;
    }

    @Override
    public List<Order> saveAll(List<Order> entity) {
        return List.of();
    }

    @SneakyThrows
    public Order findByOrderByReference(int orderRef) {
        String sql = """
                select o.id_order_as_reference
                from "order" o
                where o.id_order_as_reference = ?
                """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, orderRef);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return orderMapper.apply(rs);
                }

                throw new Exception("Order: " + orderRef + " not found");
            }
        }
    }
}
