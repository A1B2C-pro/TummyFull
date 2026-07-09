package com.tap.dao;

import com.tap.model.Admin;

public interface AdminDAO {
    Admin getAdminByEmail(String email);
}
