package com.mayhem.lms.service;

import com.mayhem.lms.constants.Status;
import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetLoanByUserIdDto;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.model.LoanStatus;
import com.mayhem.lms.model.LoanType;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.repository.LoanStatusRepository;
import com.mayhem.lms.repository.LoanTypeRepository;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final LoanStatusRepository statusRepository;
    private final LoanTypeRepository typeRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, LoanStatusRepository statusRepository, LoanTypeRepository typeRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.statusRepository = statusRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GetLoanDto updateLoan(Long id, Loan loanDetails) {
        Loan existingLoan = loanRepository.findById(id).orElse(null);
        if (existingLoan != null) {
            existingLoan.setAmount(loanDetails.getAmount());
            existingLoan.setTerm(loanDetails.getTerm());
            existingLoan.setLoanTypes(loanDetails.getLoanTypes());
//            existingLoan.setLoanStatus(loanDetails.getLoanStatus());
            Loan updatedLoan = loanRepository.save(existingLoan);
            
            return new GetLoanDto(
                updatedLoan.getId(),
                updatedLoan.getAmount(),
                updatedLoan.getTerm(),
                updatedLoan.getLoanTypes().getType(),
                updatedLoan.getLoanStatus().getStatus(),
                updatedLoan.getUsers().getFirstName() + " " + updatedLoan.getUsers().getLastName()
            );
        }
        return null;
    }

    @Override
    public Loan createLoan(CreateLoanDto newLoan) {
        Loan loan = new Loan();
        loan.setAmount(newLoan.getAmount());
        loan.setTerm(newLoan.getTerm());
        User user = userRepository.findById(newLoan.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        loan.setUsers(user);
        LoanType type = typeRepository.findById(newLoan.getType()).orElseThrow(() -> new RuntimeException("Loan type not found"));
        loan.setLoanTypes(type);
//        LoanStatus status = statusRepository.findById(newLoan.getStatusId()).orElseThrow(() -> new RuntimeException("Loan status not found"));
//        Long defaultStatus = 1L;
        LoanStatus status = new LoanStatus(1L, Status.PENDING.toString());
        loan.setLoanStatus(status);
        return loanRepository.save(loan);
    }

    @Override
    public GetLoanByUserIdDto getLoanByUserId(Long userId) {
        Optional<Loan> loan = loanRepository.findByUsersId(userId);
        if (loan.isPresent()) {
            return new GetLoanByUserIdDto(
                loan.get().getId(),
                loan.get().getAmount(),
                loan.get().getTerm(),
                loan.get().getLoanTypes().getType(),
                loan.get().getLoanStatus().getStatus(),
                loan.get().getUsers().getId()
            );
        }
        return null;
    }
}