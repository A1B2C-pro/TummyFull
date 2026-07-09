package com.tap.dao;

import com.tap.model.Cart;

public interface CartDAO {
    Cart getCartByUser(int userId);
    boolean saveCart(int userId, Cart cart);
    boolean clearCart(int userId);
}
