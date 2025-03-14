package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.User;
import com.mayhem.lms.service.AccountServiceImpl;
import com.mayhem.lms.service.UserServiceImpl;
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
    public @ResponseBody ResponseEntity<GetUserDto> registerUser(@RequestBody RegisterDto newUser){
        Account acc = accountService.createAccount(newUser);
        User createdUser = userService.createUser(newUser, acc);
        GetUserDto createdUserDto = new GetUserDto(
                acc.getEmail(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getPhone(),
                acc.getRole().getRoleName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }
}