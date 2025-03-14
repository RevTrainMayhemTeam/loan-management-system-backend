package com.mayhem.lms.service;

import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.AccountRoleRepository;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;


    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userDetails.getFirstName());
                    existingUser.setLastName(userDetails.getLastName());
                    existingUser.setPhone(userDetails.getPhone());
                    return userRepository.save(existingUser);
                });
    }
}
