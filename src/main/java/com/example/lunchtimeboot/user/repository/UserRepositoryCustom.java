package com.example.lunchtimeboot.user.repository;

import com.example.lunchtimeboot.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepositoryCustom {
    List<User> searchByName(String search);
    List<User> findByFavouriteRestaurantId(UUID favouriteRestaurantId);
}
