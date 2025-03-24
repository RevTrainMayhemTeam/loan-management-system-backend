package com.mayhem.lms.service;

import com.mayhem.lms.dto.UserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.User;
import java.util.List;

public interface UserService {
    public UserDto createUser(RegisterDto newUser, Account account);
    public List<UserDto> findAllUsers();
    public UserDto getUserById(Long id);
    public UserDto updateUser(Long id, User user);
    public boolean deleteUser(Long id);
}