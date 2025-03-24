package com.mayhem.lms.service;

import com.mayhem.lms.dto.UpdateLoanDto;
import com.mayhem.lms.model.Loan;

import java.util.Optional;

public interface LoanService {
    public UpdateLoanDto updateLoan(Long id, Loan loan);
}