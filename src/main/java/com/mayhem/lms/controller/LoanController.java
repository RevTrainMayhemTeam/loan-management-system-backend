package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.service.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.service.LoanService;


@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanServiceImpl loanServiceImpl;

    public LoanController(LoanServiceImpl loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetLoanDto> getLoanByID(@PathVariable Long id){
        GetLoanDto foundLoan = loanServiceImpl.getLoanById(id);
        if(foundLoan == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundLoan);
    }
}
