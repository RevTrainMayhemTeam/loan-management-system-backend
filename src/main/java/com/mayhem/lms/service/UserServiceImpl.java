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
    public User createUser(RegisterDto newUser, Account account) {
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPhone(newUser.getPhoneNumber());
        user.setAccount(account);
        return userRepository.save(user);
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
                    user.getPhone(),
                    user.getAccount().getRole().getRoleName()
            ));
        }
        return usersDto;
    }
}
