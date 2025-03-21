package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetLoanDto;

public interface LoanService {
    public GetLoanDto getLoanById(Long id);
}
