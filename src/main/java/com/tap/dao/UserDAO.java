package com.tap.dao;

import com.tap.model.User;
import java.util.List;

public interface UserDAO {
    boolean registerUser(User user);
    User getUserByEmail(String email);
    User getUserById(int userId);
    boolean updateProfile(User user);
    boolean updatePassword(String email, String newHashedPassword);
    boolean markVerified(String email);
    List<User> getAllUsers();
    boolean blockUser(int userId, boolean block);
    boolean deleteUser(int userId);
    boolean emailExists(String email);
}
