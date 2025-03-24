package com.mayhem.lms.service;

import com.mayhem.lms.dto.UserDto;
import com.mayhem.lms.model.Loan;

public interface LoanService {
    public UserDto createLoan(Loan newLoan);
}
