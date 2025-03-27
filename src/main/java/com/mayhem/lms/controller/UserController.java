package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.User;
import com.mayhem.lms.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        GetUserDto sessionUser = (GetUserDto) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        } else if (!"Manager".equals(sessionUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }else {
            return ResponseEntity.ok(userService.findAllUsers());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserByID(@PathVariable Long id, HttpSession session){
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        //Checks if the user is not logged in
        if(userLogged == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not logged in :(");
        }

        //Checks if the user logged in is the same from the request or if it's a Manager
        if((id.equals(userLogged.getId())) || (userLogged.getRole().equals("Manager"))){
            GetUserDto foundUser = userService.getUserById(id);
            if(foundUser == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(foundUser);
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not the right credentials :(");
    }

    /**
     * Update user details: first name, last name and phone number
     * @param id
     * @param userDetails
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Long id, @RequestBody User userDetails, HttpSession session) {
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");
        if (userLogged == null) {
            logger.info("User not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userLogged.getId() == userDetails.getId()) {
            logger.info("User logged in: {}, user details: {}", userLogged.getId(), userDetails.getId());

            if (userDetails.getFirstName() == null || userDetails.getFirstName().trim().isEmpty())
                return ResponseEntity.badRequest().build();
            if (userDetails.getLastName() == null || userDetails.getLastName().trim().isEmpty())
                return ResponseEntity.badRequest().build();
            GetUserDto updatedUser = userService.updateUser(id, userDetails);
            if (updatedUser == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long id, HttpSession session) {
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        boolean deleted = userService.deleteUser(id, userLogged);

        if (deleted){
            session.invalidate();
            return ResponseEntity.ok("User successfully deleted");
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not the right credentials :(");
        }
    }
}