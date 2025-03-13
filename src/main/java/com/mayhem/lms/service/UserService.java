package com.mayhem.lms.service;

import com.mayhem.lms.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> updateUser(Long id, User user);
}
