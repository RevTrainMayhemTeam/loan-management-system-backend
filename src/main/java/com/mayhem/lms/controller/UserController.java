package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.User;
import com.mayhem.lms.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<GetUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserByID(@PathVariable Long id){
        GetUserDto foundUser = userService.getUserById(id);
        if(foundUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        if (userDetails.getFirstName() == null || userDetails.getFirstName().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        if (userDetails.getLastName() == null || userDetails.getLastName().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        GetUserDto updatedUser = userService.updateUser(id, userDetails);
        if(updatedUser == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok("User successfully deleted") : ResponseEntity.notFound().build();
    }
}