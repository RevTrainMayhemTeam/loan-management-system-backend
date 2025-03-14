package com.mayhem.lms.controller;

import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public @ResponseBody ResponseEntity<RegisterDto> registerUser(@RequestBody RegisterDto newUser){
        System.out.println(newUser.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
