package com.mayhem.lms.controller;

import com.mayhem.lms.dto.GetLoanTypesDto;
import com.mayhem.lms.service.LoanTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loan-types")
public class LoanTypeController {

    private final LoanTypeServiceImpl loanTypeService;

    public LoanTypeController(LoanTypeServiceImpl loanTypeService) {
        this.loanTypeService = loanTypeService;
    }

    @GetMapping
    public ResponseEntity<List<GetLoanTypesDto>> getAllLoanTypes(){
        return ResponseEntity.ok().body(loanTypeService.getAllLoanTypes());
    }
}
