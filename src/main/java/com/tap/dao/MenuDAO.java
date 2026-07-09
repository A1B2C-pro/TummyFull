package com.tap.dao;

import com.tap.model.Menu;
import java.util.List;

public interface MenuDAO {
    List<Menu> getMenuByRestaurant(int restaurantId);
    Menu getMenuById(int menuId);
    List<Menu> searchMenu(String keyword);
    boolean addMenuItem(Menu menu);
    boolean updateMenuItem(Menu menu);
    boolean deleteMenuItem(int menuId);
    boolean setAvailability(int menuId, boolean available);
}
