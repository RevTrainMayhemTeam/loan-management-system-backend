package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Loan;

import java.util.List;

public interface LoanService {
    List<GetLoanDto> getAllLoans();
    GetLoanDto getLoanById(Long id, GetUserDto userLogged);
    GetLoanDto updateLoan(Long id, Loan loan);
    Loan createLoan(CreateLoanDto loan);
    List<GetLoanDto> getLoanByUserId(Long userId);
    GetLoanDto approveOrRejectLoan(Long loanId, Long statusId);
}
