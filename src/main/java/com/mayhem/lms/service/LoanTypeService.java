package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetLoanTypesDto;

import java.util.List;

public interface LoanTypeService {
    List<GetLoanTypesDto> getAllLoanTypes();
}
