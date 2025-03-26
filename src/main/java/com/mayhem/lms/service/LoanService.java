package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;

import java.util.List;

public interface LoanService {
    List<GetLoanDto> getAllLoans();
    public GetLoanDto getLoanById(Long id, GetUserDto userLogged);
    public GetUserDto createLoan(Loan newLoan);
}
