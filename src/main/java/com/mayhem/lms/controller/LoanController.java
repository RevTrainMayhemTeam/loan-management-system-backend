package com.mayhem.lms.controller;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.UpdateLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanService;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/loans", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanService = loanServiceImpl;
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetLoanDto> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
        GetLoanDto updatedLoan = loanService.updateLoan(id, loanDetails);
        if(updatedLoan == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLoan);
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto newLoan) {
        try {
            Loan loan = loanService.createLoan(newLoan);
            GetLoanDto response = new GetLoanDto(
                    loan.getId(),
                    loan.getAmount(),
                    loan.getTerm(),
                    loan.getLoanTypes().getType(),
                    loan.getLoanStatus().getStatus(),
                    loan.getUsers().getFirstName() + " " + loan.getUsers().getLastName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}