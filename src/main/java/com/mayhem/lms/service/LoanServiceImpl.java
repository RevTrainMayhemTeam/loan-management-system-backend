package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.UpdateLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.model.LoanStatus;
import com.mayhem.lms.model.LoanType;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.repository.LoanStatusRepository;
import com.mayhem.lms.repository.LoanTypeRepository;
import com.mayhem.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

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
    public UpdateLoanDto updateLoan(Long id, Loan loanDetails) {
        Loan existingLoan = loanRepository.findById(id).orElse(null);
        if (existingLoan != null) {
            existingLoan.setAmount(loanDetails.getAmount());
            existingLoan.setTerm(loanDetails.getTerm());
            existingLoan.setLoanTypes(loanDetails.getLoanTypes());
            existingLoan.setLoanStatus(loanDetails.getLoanStatus());
            loanRepository.save(existingLoan);

            Loan updatedLoan = loanRepository.findById(id).orElse(null);
            if (updatedLoan != null) {
                return new UpdateLoanDto(
                        updatedLoan.getAmount(),
                        updatedLoan.getTerm(),
                        updatedLoan.getLoanTypes(),
                        updatedLoan.getLoanStatus()
                );
            } return null;
        } return null;
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
        LoanStatus status = statusRepository.findById(newLoan.getStatus()).orElseThrow(() -> new RuntimeException("Loan status not found"));
        loan.setLoanStatus(status);
        return loanRepository.save(loan);
    }
}