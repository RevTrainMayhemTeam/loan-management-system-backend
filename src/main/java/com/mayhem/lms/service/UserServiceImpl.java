package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetUserDto createUser(RegisterDto newUser, Account account) {
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setAccount(account);
        User createdUser = userRepository.save(user);
        return new GetUserDto(
                account.getEmail(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getPhoneNumber(),
                account.getRole().getRoleName()
        );
    }

    @Override
    public List<GetUserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<GetUserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(new GetUserDto(
                    user.getAccount().getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber(),
                    user.getAccount().getRole().getRoleName()
            ));
        }
        return usersDto;
    }

    @Override
    public GetUserDto getUserById(Long id){
        User foundUser = userRepository.findById(id).orElse(null);
        if (foundUser!=null){
            return new GetUserDto(
                    foundUser.getAccount().getEmail(),
                    foundUser.getFirstName(),
                    foundUser.getLastName(),
                    foundUser.getPhoneNumber(),
                    foundUser.getAccount().getRole().getRoleName()
            );
        } else return null;
    }

    @Override
    public GetUserDto updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null){
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            userRepository.save(existingUser);
            User updatedUser = userRepository.findById(id).orElse(null);
            if (updatedUser != null){
                return new GetUserDto(
                        updatedUser.getAccount().getEmail(),
                        updatedUser.getFirstName(),
                        updatedUser.getLastName(),
                        updatedUser.getPhoneNumber(),
                        updatedUser.getAccount().getRole().getRoleName()
                );
            } else return null;
        } else return null;
    }

    @Override
    public boolean deleteUser(Long id){
        return userRepository.findById(id).map(profile -> {
            userRepository.delete(profile);
            return true;
        }).orElse(false);
    }
}