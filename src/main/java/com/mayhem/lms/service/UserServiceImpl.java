package com.mayhem.lms.service;

import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
