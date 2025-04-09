package com.resto_spring_boot.service;

import com.resto_spring_boot.dao.operations.DishDAO;
import com.resto_spring_boot.models.dish.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO dishDAO;

    public List<Dish> getAllDish(int page, int size) {
        return dishDAO.getAll(page, size);
    }
}
