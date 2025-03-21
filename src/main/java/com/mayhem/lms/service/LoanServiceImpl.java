package com.mayhem.lms.service;

import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
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
    public GetLoanDto getLoanById(Long id){
        Loan foundedLoan = loanRepository.findById(id).orElse(null);

        Long userIDByLoan = foundedLoan.getUsers().getId();
        User usersLoan = userRepository.findById(userIDByLoan).orElse(null);

        String userRole = usersLoan.getAccount().getRole().getRoleName();
        String usersName = usersLoan.getFirstName() + " " + usersLoan.getLastName();

        //Validate if the user is a Customer
        if(userRole.equals("Customer")){
                return new GetLoanDto(foundedLoan.getAmount(),
                        foundedLoan.getTerm(),
                        foundedLoan.getLoanTypes().getType(),
                        usersName);
        }
        //Validate if the user is a Manager
        if(userRole.equals("Manager")){
            return new GetLoanDto(foundedLoan.getAmount(),
                    foundedLoan.getTerm(),
                    foundedLoan.getLoanTypes().getType(),
                    usersName);
        }
        else return null;
    }
}
