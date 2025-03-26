package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.service.AccountServiceImpl;
import com.mayhem.lms.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final AccountServiceImpl accountService;

    public AuthController(UserServiceImpl userService, AccountServiceImpl accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping(path = "/register")
    public @ResponseBody ResponseEntity<GetUserDto> registerUser(@Valid @RequestBody RegisterDto newUser){
        if (newUser.getEmail().trim().isEmpty() || newUser.getEmail() == null)
            return ResponseEntity.badRequest().build();
        if (newUser.getPassword().trim().isEmpty() || newUser.getPassword() == null)
            return ResponseEntity.badRequest().build();
        if (newUser.getFirstName().trim().isEmpty() || newUser.getFirstName() == null)
            return ResponseEntity.badRequest().build();
        if (newUser.getLastName().trim().isEmpty() || newUser.getLastName() == null)
            return ResponseEntity.badRequest().build();
        if (newUser.getRoleId() == null)
            return ResponseEntity.badRequest().build();
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
}