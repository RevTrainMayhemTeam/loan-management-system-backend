package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.model.User;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mayhem.lms.dto.GetLoanDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLoans(HttpSession session){
        GetUserDto sessionUser = (GetUserDto) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        } else if (!"Customer".equals(sessionUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }else {
            return ResponseEntity.ok(loanServiceImpl.getAllLoans());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanByID(@PathVariable Long id, HttpSession session){
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        //Checks if the user is not logged in
        if(userLogged == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not logged in :(");
        }

        //Checks if the user is a Manager
        GetLoanDto foundLoan = loanServiceImpl.getLoanById(id, userLogged);
        //Checks if the loan exists
        if(foundLoan == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not the right credentials :(");
        }
        return ResponseEntity.ok(foundLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id, HttpSession session) {
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        if(userLogged == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not logged in :(");
        }

        boolean deleted = loanServiceImpl.deleteLoan(id, userLogged);
        return deleted ? ResponseEntity.ok("Loan successfully deleted") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not the right credentials :(");
    }
}
