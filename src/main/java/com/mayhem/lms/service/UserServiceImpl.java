package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user
     * @param newUser
     * @param account
     * @return
     */
    @Override
    public GetUserDto createUser(RegisterDto newUser, Account account) {
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setAccount(account);
        User createdUser = userRepository.save(user);
        logger.info("User created successfully with id: {}", user.getId());
        return new GetUserDto(
                createdUser.getId(),
                account.getEmail(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getPhoneNumber(),
                account.getRole().getRoleName()
        );
    }

    /**
     * Find all users
     * @return
     */
    @Override
    public List<GetUserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<GetUserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(new GetUserDto(
                    user.getId(),
                    user.getAccount().getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber(),
                    user.getAccount().getRole().getRoleName()
            ));
        }
        return usersDto;
    }

    /**
     * Find a user by id
     * @param id
     * @return
     */
    @Override
    public GetUserDto getUserById(Long id){
        User foundUser = userRepository.findById(id).orElse(null);
        if (foundUser!=null){
            return new GetUserDto(
                    foundUser.getId(),
                    foundUser.getAccount().getEmail(),
                    foundUser.getFirstName(),
                    foundUser.getLastName(),
                    foundUser.getPhoneNumber(),
                    foundUser.getAccount().getRole().getRoleName()
            );
        } else return null;
    }

    /**
     * Update a user, only first name, last name and phone number can be updated
     * @param id
     * @param userDetails
     * @return
     */
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
                        updatedUser.getId(),
                        updatedUser.getAccount().getEmail(),
                        updatedUser.getFirstName(),
                        updatedUser.getLastName(),
                        updatedUser.getPhoneNumber(),
                        updatedUser.getAccount().getRole().getRoleName()
                );
            } else return null;
        } else {
            logger.info("No user found");
            return null;
        }
    }

    /**
     * Delete a user
     * @param id
     * @return
     */
    @Override
    public boolean deleteUser(Long id){
        User userToDelete = userRepository.findById(id).orElse(null);
        logger.info("Deleting user with id: {}", id);
        List<?> userLoans = userToDelete.getLoans();
        boolean hasApprovedLoans = userLoans.stream()
                .anyMatch(loan -> ((Loan) loan).getLoanStatus().getStatus().equals("Approved"));
        if (hasApprovedLoans) {
            logger.info("User has active loans, cannot delete");
            return false;
        }
        userRepository.delete(userToDelete);
        logger.info("User with id {} deleted", id);
        return true;
    }
}