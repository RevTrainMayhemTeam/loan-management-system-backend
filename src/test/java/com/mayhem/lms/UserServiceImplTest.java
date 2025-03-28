package com.mayhem.lms;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.UserRepository;
import com.mayhem.lms.service.UserServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User userTest;
    private Account accountTest;
    private AccountRole accountRoleTest;

    @BeforeEach
    public void setUp() {
        userTest = new User();

        userTest.setId(1L);
        userTest.setFirstName("Manu");
        userTest.setLastName("Rios");
        userTest.setPhoneNumber("1234567890");

        accountRoleTest = new AccountRole();
        accountRoleTest.setRoleName("Manager");

        accountTest = new Account();
        //accountTest.setEmail("manu.rios@example.com"); // It's good practice to set the email as well
        accountTest.setRole(accountRoleTest);

        userTest.setAccount(accountTest);


    }

    @Test
    public void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userTest));
        List<GetUserDto> users = userServiceImpl.findAllUsers();

        assertNotEquals(2, users.size(), "The list should contain only 1 user");
        assertEquals("Manu", users.get(0).getFirstName(), "The list should contain user user named Manu");
        // Verify that findAll() on the repository was called exactly once.
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        boolean result = userServiceImpl.deleteUser(1L);

        assertTrue(result, "Should be able to delete user then is true");
        // Verify that findAll() on the repository was called exactly once.
        verify(userRepository, times(1)).findById(any());

    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        GetUserDto user = userServiceImpl.getUserById(1L);
        assertEquals("Manu", user.getFirstName(), "The list should contain user user named Manu");
        // Verify that findAll() on the repository was called exactly once.
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(4L)).thenReturn(Optional.empty());
        GetUserDto user = userServiceImpl.getUserById(4L);
        assertNull(user, "Any user with that id exist, should return null");
        // Verify that findAll() on the repository was called exactly once.
        verify(userRepository, times(1)).findById(any());
    }
}