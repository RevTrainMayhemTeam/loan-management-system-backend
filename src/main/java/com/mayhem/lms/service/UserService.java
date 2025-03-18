package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public GetUserDto createUser(RegisterDto newUser, Account account);
    public List<GetUserDto> findAllUsers();
    public boolean deleteUser(Long id);
    public GetUserDto getUserById(Long id);
    public GetUserDto updateUser(Long id, User user);
}
