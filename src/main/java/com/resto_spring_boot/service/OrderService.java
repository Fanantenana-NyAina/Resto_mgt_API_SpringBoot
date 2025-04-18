package com.resto_spring_boot.service;

import com.resto_spring_boot.dao.operations.order.OrderDAO;
import com.resto_spring_boot.models.order.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderDAO orderDAO;

    public Order getOrderByReference(int orderRef) {
        return orderDAO.findByOrderByReference(orderRef);
    }
}
