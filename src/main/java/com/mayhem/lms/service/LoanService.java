package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.UpdateLoanDto;
import com.mayhem.lms.model.Loan;

public interface LoanService {
    UpdateLoanDto updateLoan(Long id, Loan loan);
    Loan createLoan(CreateLoanDto loan);
}