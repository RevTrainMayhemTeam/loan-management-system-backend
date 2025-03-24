package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.dto.UpdateLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanService;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanService = loanServiceImpl;
    }


    /**
     * @json
     * {
     *     "amount": 1,
     *     "term": 1,
     *     "type": "PERSONAL",
     *     "status": "APPROVED",
     * }
     * @param id
     * @param loanDetails
     * @return
     */
    @PutMapping({"/{id}"})
    public ResponseEntity<UpdateLoanDto> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
        UpdateLoanDto updatedLoan = loanService.updateLoan(id, loanDetails);
        if(updatedLoan == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLoan);
    }
}