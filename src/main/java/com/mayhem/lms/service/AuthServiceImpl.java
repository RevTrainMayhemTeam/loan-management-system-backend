package com.mayhem.lms.service;

import com.mayhem.lms.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userServiceImpl;

    public AuthServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
}
