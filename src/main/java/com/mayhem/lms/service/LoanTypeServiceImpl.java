package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetLoanTypesDto;
import com.mayhem.lms.model.LoanType;
import com.mayhem.lms.repository.LoanTypesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanTypeServiceImpl implements LoanTypeService{

    LoanTypesRepository loanTypesRepository;

    public LoanTypeServiceImpl(LoanTypesRepository loanTypesRepository) {
        this.loanTypesRepository = loanTypesRepository;
    }

    /**
     * Retrieve all loan types from the database and convert them into GetLoanTypesDto objects
     * @return List of GetLoanTypesDto
     */
    @Override
    public List<GetLoanTypesDto> getAllLoanTypes() {
        List<LoanType> types = loanTypesRepository.findAll();
        List<GetLoanTypesDto> typesDto = new ArrayList<>();
        for (LoanType type : types) {
            typesDto.add(new GetLoanTypesDto(
                    type.getId(),
                    type.getType()
            ));
        }
        return typesDto;
    }
}
