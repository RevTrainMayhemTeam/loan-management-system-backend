package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;

public interface LoanService {
    GetLoanDto updateLoan(Long id, Loan loan);
    Loan createLoan(CreateLoanDto loan);
}