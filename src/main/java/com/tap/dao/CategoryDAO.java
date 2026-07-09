package com.tap.dao;

import com.tap.model.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> getAllCategories();
    Category getCategoryById(int categoryId);
    boolean addCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(int categoryId);
}
