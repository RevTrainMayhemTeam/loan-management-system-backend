package com.mayhem.lms.controller;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping(value = "/api/loans", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @GetMapping
    public ResponseEntity<?> getAllLoans(HttpSession session){
        GetUserDto sessionUser = (GetUserDto) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        } else if (!"Manager".equals(sessionUser.getRole())) {
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
        GetLoanDto updatedLoan = loanServiceImpl.updateLoan(id, loanDetails);
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
            Loan loan = loanServiceImpl.createLoan(newLoan);
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
        List<GetLoanDto> foundLoan = loanServiceImpl.getLoanByUserId(userId);
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
    @PatchMapping(value = "/{loanId}/reject")
    public ResponseEntity rejectLoan(@PathVariable Long loanId) {
        return approveOrRejectLoan(loanId, 3L);
    }

    /**
     * Approve loan, call approveOrRejectLoan with statusId 2
     * @param loanId = 2L
     * @return approveOrRejectLoan
     */
    @PatchMapping(value = "/{loanId}/approve")
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
        GetLoanDto response = loanServiceImpl.approveOrRejectLoan(loanId, statusId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }
}
