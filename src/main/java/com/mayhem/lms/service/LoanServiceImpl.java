package com.mayhem.lms.service;

import com.mayhem.lms.dto.UserDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }


    @Override
    public UserDto createLoan(Loan newLoan) {
        return null;
    }
}
