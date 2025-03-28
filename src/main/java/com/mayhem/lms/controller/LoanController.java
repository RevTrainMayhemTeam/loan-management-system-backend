package com.mayhem.lms.controller;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping(value = "/api/loans", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

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
        return deleted ? ResponseEntity.ok("Loan successfully deleted") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or loan is active");
    }

    /**
     * Update loan by id, it updates only amount, term and type. Only user can update its own loan
     * @param id
     * @param loanDetails
     * @return
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails, HttpSession session) {
        //Check if user is logged in
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");
        if (userLogged == null) {
            logger.error("Not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        //Validate loan details
        logger.info("Updating loan with id: {}", id);
        if (loanDetails.getAmount() == null) {
            logger.error("Amount must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must not be null");
        }
        if (loanDetails.getTerm() == null) {
            logger.error("Term must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Term must not be null");
        }
        if (loanDetails.getLoanTypes().getId() == null) {
            logger.error("Loan type must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loan type must not be null");
        }

        GetLoanDto updatedLoan = null;
        try {
            updatedLoan = loanServiceImpl.updateLoan(id, loanDetails, userLogged);
        } catch (NullPointerException e) {
            logger.error("Loan not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No loan found with id:" + id);
        }

        if (updatedLoan == null) {
            logger.error("Unauthorized access to update loan {}", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to update loan ");
        }
        logger.info("Loan with id: {} updated successfully", id);
        return ResponseEntity.ok(updatedLoan);
    }

    /**
     * Create a new loan
     * @param newLoan
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto newLoan, HttpSession session) {
        //Check if user is logged in
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");
        if (userLogged == null) {
            logger.error("Not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        if (newLoan.getUserId() != userLogged.getId()) {
            logger.error("Unauthorized access to create loan for user Id: {}", newLoan.getUserId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        logger.info("Creating a new loan");

        //Validate loan details in payload
        if (newLoan.getAmount() == null) {
            logger.error("Amount must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must not be null");
        }
        if (newLoan.getTerm() == null) {
            logger.error("Loan term must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loan term must not be null");
        }
        if (newLoan.getType() == null) {
            logger.error("Loan type must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loan type must not be null");
        }
        if (newLoan.getUserId() == null) {
            logger.error("User id must not be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User id must not be null");
        }
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
    public ResponseEntity<List<?>> getLoanByUserId(@PathVariable Long userId, HttpSession session) {
        //Check if user is logged in
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");
        if (userLogged == null) {
            logger.error("Not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //Check if user is either the owner of the loans or a manager
        if (!userId.equals(userLogged.getId()) && !"Manager".equals(userLogged.getRole())) {
            logger.error("Unauthorized access to get loans for userId: {}, userSessionId: {}", userId, userLogged.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GetLoanDto> foundLoan = loanServiceImpl.getLoanByUserId(userId);
        if (foundLoan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foundLoan);
    }

    /**
     * Reject loan, call approveOrRejectLoan with statusId 3
     * @param loanId = 3L
     * @return approveOrRejectLoan
     */
    @PatchMapping(value = "/{loanId}/reject")
    public ResponseEntity rejectLoan(@PathVariable Long loanId, HttpSession session) {
        //Check if user is logged in
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        logger.info("Loan rejected with id: {}", loanId);
        return approveOrRejectLoan(loanId, 3L, userLogged);
    }

    /**
     * Approve loan, call approveOrRejectLoan with statusId 2
     * @param loanId = 2L
     * @return approveOrRejectLoan
     */
    @PatchMapping(value = "/{loanId}/approve")
    public ResponseEntity approveLoan(@PathVariable Long loanId, HttpSession session) {
        //Check if user is logged in
        GetUserDto userLogged = (GetUserDto) session.getAttribute("user");

        logger.info("Loan approved with id: {}", loanId);
        return approveOrRejectLoan(loanId, 2L, userLogged);
    }

    /**
     * Method to approve or reject loan
     * @param loanId
     * @param statusId
     * @return ResponseEntity
     */
    public ResponseEntity<?> approveOrRejectLoan(Long loanId, Long statusId, GetUserDto userLogged) {
        //Validate session
        if (userLogged == null) {
            logger.error("Not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        //Check if user is a manager
        if(!userLogged.getRole().equals("Manager")) {
            logger.error("Unauthorized access");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        GetLoanDto response = loanServiceImpl.approveOrRejectLoan(loanId, statusId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }
}
