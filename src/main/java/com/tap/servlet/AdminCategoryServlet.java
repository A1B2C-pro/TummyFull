package com.tap.servlet;

import com.tap.dao.CategoryDAO;
import com.tap.daoimpl.CategoryDAOImpl;
import com.tap.model.Category;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminCategoryServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/manage-categories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "add": {
                Category c = new Category(request.getParameter("categoryName"), request.getParameter("imageUrl"));
                categoryDAO.addCategory(c);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Category added.");
                break;
            }
            case "edit": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("categoryId"));
                Category c = new Category(request.getParameter("categoryName"), request.getParameter("imageUrl"));
                c.setCategoryId(id);
                categoryDAO.updateCategory(c);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Category updated.");
                break;
            }
            case "delete": {
                int id = ValidationUtil.parseIntOrZero(request.getParameter("categoryId"));
                categoryDAO.deleteCategory(id);
                request.getSession().setAttribute(SessionConstants.SUCCESS_MSG, "Category deleted.");
                break;
            }
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/AdminCategoryServlet");
    }
}
