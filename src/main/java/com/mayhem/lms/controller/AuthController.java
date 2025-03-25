package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.service.AccountServiceImpl;
import com.mayhem.lms.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GetUserDto> registerUser(@RequestBody RegisterDto newUser){
        Account acc = accountService.createAccount(newUser);
        GetUserDto createdUser = userService.createUser(newUser, acc);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthDto userCredentials, HttpSession session){
        if (accountService.verifyCredentials(userCredentials)) {
            Account account = accountService.getAccountByEmail(userCredentials.getEmail());
            GetUserDto loggedUser = userService.getUserById(account.getUser().getId());
            session.setAttribute("user", loggedUser);
            return ResponseEntity.ok("Login Successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        GetUserDto loggedUser = (GetUserDto) session.getAttribute("user");
        if (loggedUser != null){
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully");
        } else{
            return ResponseEntity.notFound().build();
        }
    }

}