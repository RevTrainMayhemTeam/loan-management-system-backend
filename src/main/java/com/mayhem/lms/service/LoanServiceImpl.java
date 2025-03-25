package com.mayhem.lms.service;

import com.mayhem.lms.dto.UpdateLoanDto;
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
    public Loan createLoan(Loan loan) {
        return loanRepository.save(loan);
    }
}