package com.mayhem.lms.controller;

import com.mayhem.lms.model.User;
import com.mayhem.lms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserByID(@PathVariable Long id){
        Optional<User> foundedUser = userService.getUserById(id);
        if(foundedUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundedUser);
    }

}
