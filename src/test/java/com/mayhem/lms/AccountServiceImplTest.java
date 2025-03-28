package com.mayhem.lms;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.repository.AccountRepository;
import com.mayhem.lms.repository.RoleRepository;
import com.mayhem.lms.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private Account account;
    private RegisterDto registerDtoTest;
    private AccountRole roleTest; // Corrected variable name for clarity
    private AuthDto authDtoTest;

    @BeforeEach
    public void setUp() {
        registerDtoTest = new RegisterDto();
        registerDtoTest.setEmail("test@test.com");
        registerDtoTest.setPassword("password");
        registerDtoTest.setFirstName("Test");
        registerDtoTest.setLastName("Test");
        registerDtoTest.setPhoneNumber("1234567890");
        registerDtoTest.setRoleId(1L);

        account = new Account();
        account.setEmail(registerDtoTest.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(registerDtoTest.getPassword()));

        roleTest = new AccountRole();
        roleTest.setId(registerDtoTest.getRoleId());
        roleTest.setRoleName("Manager"); // Example role name
        account.setRole(roleTest);

        authDtoTest = new AuthDto();
        authDtoTest.setEmail(registerDtoTest.getEmail());
        authDtoTest.setPassword(registerDtoTest.getPassword());
    }

    @Test
    public void testCreateAccount() {
        // Arrange
        when(roleRepository.findById(registerDtoTest.getRoleId())).thenReturn(Optional.of(roleTest));
        when(accountRepository.save(any(Account.class))).thenReturn(account); // Use any(Account.class)

        // Act
        Account createdAccount = accountServiceImpl.createAccount(registerDtoTest);

        // Assert
        assertNotNull(createdAccount);

        // Verify interactions
        verify(roleRepository, times(1)).findById(registerDtoTest.getRoleId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_InvalidEmailFormat() {
        // Arrange
        registerDtoTest.setEmail("invalid-email");

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountServiceImpl.createAccount(registerDtoTest);
        });
        assertEquals("Invalid email format", exception.getMessage());

        // Verify interactions
        verify(roleRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testVerifyCredentials() {
        when(accountRepository.findByEmail(registerDtoTest.getEmail())).thenReturn(Optional.of(account));

        boolean credentials = accountServiceImpl.verifyCredentials(authDtoTest);

        assertTrue(credentials, "Pass if the an account exists with the given credentials");

        verify(accountRepository, times(1)).findByEmail(registerDtoTest.getEmail());

    }

    @Test
    public void testGetAccountByEmail() {
        when(accountRepository.findByEmail(registerDtoTest.getEmail())).thenReturn(Optional.of(account));

        Account accountByEmail = accountServiceImpl.getAccountByEmail(account.getEmail());

        assertNotNull(accountByEmail);
        assertEquals(account.getEmail(), accountByEmail.getEmail());

        verify(accountRepository, times(1)).findByEmail(registerDtoTest.getEmail());

    }
}