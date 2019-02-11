package com.example.lunchtimeboot.restaurant.repository;

import com.example.lunchtimeboot.restaurant.entity.Restaurant;

import java.util.List;

public interface RestaurantRepositoryCustom {
    List<Restaurant> searchByName(String search);
}
