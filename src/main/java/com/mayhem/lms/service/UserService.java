package com.mayhem.lms.service;

import com.mayhem.lms.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
}
