package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanByID(@PathVariable Long id, HttpSession session){
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");
        GetLoanDto foundLoan = loanServiceImpl.getLoanById(id);

        //Checks if the user is not logged in
        if(userLogged == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not logged in :(");
        }
        //Checks if the loan exists
        if(foundLoan == null){
            return ResponseEntity.notFound().build();
        }

        //Checks if the user logged in is the same from the request or if it's a Manager - DOES NOT WORK
        if((id.equals(userLogged.getId())) || (userLogged.getRole().equals("Manager"))){
            return ResponseEntity.ok(foundLoan);
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not the right credentials :(");
    }
}
