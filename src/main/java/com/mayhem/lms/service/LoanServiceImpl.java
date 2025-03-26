package com.mayhem.lms.service;

import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<GetLoanDto> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<GetLoanDto> loanDto = new ArrayList<>();
        for (Loan loan : loans) {
            Optional<User> optionalUser = userRepository.findById(loan.getUsers().getId());

            User user = optionalUser.get();
            String userName = user.getFirstName() + " " + user.getLastName();
            loanDto.add(new GetLoanDto(
                    loan.getId(),
                    loan.getAmount(),
                    loan.getTerm(),
                    loan.getLoanTypes().getType(),
                    loan.getLoanStatus().getStatus(),
                    userName
            ));
        }
        return loanDto;
    }

    @Override
    public GetLoanDto getLoanById(Long id, GetUserDto userLogged){
        Loan foundedLoan = loanRepository.findById(id).orElse(null);
        Long userIDByLoan = foundedLoan.getUsers().getId();
        User usersLoan = userRepository.findById(userIDByLoan).orElse(null);

        String userRole = usersLoan.getAccount().getRole().getRoleName();
        String usersName = usersLoan.getFirstName() + " " + usersLoan.getLastName();

        //Checks if the user logged is the same user stored in the loan or if it is a Manager
        if((userLogged.getId().equals(userIDByLoan)) || (userLogged.getRole().equals("Manager"))){
            return new GetLoanDto(foundedLoan.getId(),
                    foundedLoan.getAmount(),
                    foundedLoan.getTerm(),
                    foundedLoan.getLoanTypes().getType(),
                    foundedLoan.getLoanStatus().getStatus(),
                    usersName);
        }
        else return null;
    }

    @Override
    public boolean deleteLoan(Long loanId, GetUserDto userLogged){
        Loan loanToDelete = loanRepository.findById(loanId).orElse(null);
        Long ownerId = loanToDelete.getUsers().getId();

        if(userLogged.getId().equals(ownerId)){
            loanRepository.delete(loanToDelete);
            return true;
        }else{
            return false;
        }
    }
}
