package com.tap.dao;

import com.tap.model.Restaurant;
import java.util.List;

public interface RestaurantDAO {
    List<Restaurant> getAllRestaurants();
    List<Restaurant> getActiveRestaurants();
    Restaurant getRestaurantById(int restaurantId);
    List<Restaurant> searchRestaurants(String keyword);
    List<Restaurant> getRestaurantsByCategory(int categoryId);
    List<Restaurant> getTopRatedRestaurants(int limit);
    boolean addRestaurant(Restaurant restaurant);
    boolean updateRestaurant(Restaurant restaurant);
    boolean deleteRestaurant(int restaurantId);
    boolean setActive(int restaurantId, boolean active);
    boolean setOpen(int restaurantId, boolean open);
}
