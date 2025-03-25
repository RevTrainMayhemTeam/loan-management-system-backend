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
        if (loanDetails.getAmount() == null)
            return ResponseEntity.badRequest().build();
        if (loanDetails.getTerm() == null)
            return ResponseEntity.badRequest().build();
        if (loanDetails.getLoanTypes() == null || loanDetails.getLoanTypes().getId() == null || loanDetails.getLoanTypes().getType() == null || loanDetails.getLoanTypes().getType().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        if (loanDetails.getLoanStatus() == null || loanDetails.getLoanStatus().getId() == null || loanDetails.getLoanStatus().getStatus() == null || loanDetails.getLoanStatus().getStatus().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        GetLoanDto updatedLoan = loanService.updateLoan(id, loanDetails);
        if (updatedLoan == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedLoan);
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto newLoan) {
        if (newLoan.getAmount() == null)
            return ResponseEntity.badRequest().build();
        if (newLoan.getTerm() == null)
            return ResponseEntity.badRequest().build();
        if (newLoan.getType() == null)
            return ResponseEntity.badRequest().build();
        if (newLoan.getStatus() == null)
            return ResponseEntity.badRequest().build();
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