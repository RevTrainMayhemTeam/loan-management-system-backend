package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GetUserDto createLoan(Loan newLoan) {
        return null;
    }

    @Override
    public GetLoanDto getLoanById(Long id){
        Loan foundedLoan = loanRepository.findById(id).orElse(null);

        Long userIDByLoan = foundedLoan.getUsers().getId();
        User usersLoan = userRepository.findById(userIDByLoan).orElse(null);

        String userRole = usersLoan.getAccount().getRole().getRoleName();
        String usersName = usersLoan.getFirstName() + " " + usersLoan.getLastName();

        return new GetLoanDto(foundedLoan.getId(),
                foundedLoan.getAmount(),
                foundedLoan.getTerm(),
                foundedLoan.getLoanTypes().getType(),
                usersName);
    }
}
