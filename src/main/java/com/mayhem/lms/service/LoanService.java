package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;

public interface LoanService {
    public GetLoanDto getLoanById(Long id, GetUserDto userLogged);
    public GetUserDto createLoan(Loan newLoan);
}
