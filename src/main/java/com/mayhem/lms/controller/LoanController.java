package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @PutMapping({"/id"})
    public ResponseEntity<GetLoanDto> updateLoanById(@PathVariable Long id, @RequestBody Loan loanDetails) {

    }
}