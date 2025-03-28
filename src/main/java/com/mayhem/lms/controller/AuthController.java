package com.mayhem.lms.controller;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.service.AccountServiceImpl;
import com.mayhem.lms.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserServiceImpl userService;
    private final AccountServiceImpl accountService;

    public AuthController(UserServiceImpl userService, AccountServiceImpl accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * Creates a new user, valid email address must be provided
     * @param newUser
     * @return
     */
    @PostMapping(path = "/register")
    public @ResponseBody ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto newUser){
        if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");
        if (newUser.getFirstName() == null || newUser.getFirstName().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name is required");
        if (newUser.getLastName() == null || newUser.getLastName().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name is required");
        if (newUser.getRoleId() == null)
            newUser.setRoleId(2L);
        Account acc = accountService.createAccount(newUser);
        GetUserDto createdUser = userService.createUser(newUser, acc);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Login user with email and password
     * @param userCredentials
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthDto userCredentials, HttpSession session){
        if ( userCredentials.getEmail() == null || userCredentials.getEmail().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        if (userCredentials.getPassword() == null || userCredentials.getPassword().trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");

        if (accountService.verifyCredentials(userCredentials)) {
            Account account = accountService.getAccountByEmail(userCredentials.getEmail());
            GetUserDto loggedUser = userService.getUserById(account.getUser().getId());
            session.setAttribute("user", loggedUser);
            logger.info("Session started for userId: {}", loggedUser.getId());
            return ResponseEntity.ok(loggedUser);
        } else {
            logger.error("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    /**
     * Check if the user is logged in
     * @param session
     * @return
     */
    @GetMapping("/session-check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        GetUserDto loggedUser = (GetUserDto) session.getAttribute("user");
        if (loggedUser != null) {
            return ResponseEntity.ok(loggedUser);// Return user details if logged in
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session invalid or expired");
        }
    }


    /**
     * Logout user
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        GetUserDto loggedUser = (GetUserDto) session.getAttribute("user");
        if (loggedUser != null){
            logger.info("Closing session for userId: {}", loggedUser.getId());
            session.invalidate();
            logger.info("Session closed");
            return ResponseEntity.ok("Logged out successfully");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active session found");
        }
    }
}