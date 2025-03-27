package com.mayhem.lms.controller;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanService;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/loans", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanService = loanServiceImpl;
    }

    /**
     * Update loan by id, it updates only amount, term and type
     * @param id
     * @param loanDetails
     * @return
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetLoanDto> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
        if (loanDetails.getAmount() == null)
            return ResponseEntity.badRequest().build();
        if (loanDetails.getTerm() == null)
            return ResponseEntity.badRequest().build();
        if (loanDetails.getLoanTypes() == null || loanDetails.getLoanTypes().getId() == null || loanDetails.getLoanTypes().getType() == null || loanDetails.getLoanTypes().getType().trim().isEmpty())
            return ResponseEntity.badRequest().build();
        GetLoanDto updatedLoan = loanService.updateLoan(id, loanDetails);
        if (updatedLoan == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedLoan);
    }

    /**
     * Create a new loan
     * @param newLoan
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto newLoan) {
        if (newLoan.getAmount() == null)
            return ResponseEntity.badRequest().build();
        if (newLoan.getTerm() == null)
            return ResponseEntity.badRequest().build();
        if (newLoan.getType() == null)
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

    /**
     * Get all loans by User id
     * @param userId
     * @return
     */
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<?>> getLoanByUserId(@PathVariable Long userId) {
        List<GetLoanDto> foundLoan = loanService.getLoanByUserId(userId);
        if (foundLoan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundLoan);
    }

    /**
     * Reject loan, call approveOrRejectLoan with statusId 3
     * @param loanId = 3L
     * @return approveOrRejectLoan
     */
    @PutMapping(value = "/{loanId}/reject")
    public ResponseEntity rejectLoan(@PathVariable Long loanId) {
        return approveOrRejectLoan(loanId, 3L);
    }

    /**
     * Approve loan, call approveOrRejectLoan with statusId 2
     * @param loanId = 2L
     * @return approveOrRejectLoan
     */
    @PutMapping(value = "/{loanId}/approve")
    public ResponseEntity approveLoan(@PathVariable Long loanId) {
        return approveOrRejectLoan(loanId, 2L);
    }

    /**
     * Method to approve or reject loan
     * @param loanId
     * @param statusId
     * @return ResponseEntity
     */
    public ResponseEntity<?> approveOrRejectLoan(Long loanId, Long statusId) {
        GetLoanDto response = loanService.approveOrRejectLoan(loanId, statusId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }
}