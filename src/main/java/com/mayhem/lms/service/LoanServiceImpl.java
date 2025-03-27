package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Update loan
     * @param id
     * @param loanDetails
     * @return
     */
    @Override
    public GetLoanDto updateLoan(Long id, Loan loanDetails) {
        Loan existingLoan = loanRepository.findById(id).orElse(null);
        if (existingLoan != null) {
            existingLoan.setAmount(loanDetails.getAmount());
            existingLoan.setTerm(loanDetails.getTerm());
            existingLoan.setLoanTypes(loanDetails.getLoanTypes());
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

    /**
     * Create a new loan
     * @param newLoan
     * @return
     */
    @Override
    public Loan createLoan(CreateLoanDto newLoan) {
        Loan loan = new Loan();
        loan.setAmount(newLoan.getAmount());
        loan.setTerm(newLoan.getTerm());
        User user = userRepository.findById(newLoan.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        loan.setUsers(user);
        LoanType type = typeRepository.findById(newLoan.getType()).orElseThrow(() -> new RuntimeException("Loan type not found"));
        loan.setLoanTypes(type);
        LoanStatus status = statusRepository.findById(1L).orElseThrow(() -> new RuntimeException("Loan status not found"));
        loan.setLoanStatus(status);
        return loanRepository.save(loan);
    }

    /**
     *  Get all loans by user id
     * @param userId
     * @return
     */
    @Override
    public List<GetLoanDto> getLoanByUserId(Long userId) {
        Optional<List<Loan>> loans = loanRepository.findByUsersId(userId);
        if (loans.isPresent()) {
            return loans.get().stream().map(loan -> new GetLoanDto(
                loan.getId(),
                loan.getAmount(),
                loan.getTerm(),
                loan.getLoanTypes().getType(),
                loan.getLoanStatus().getStatus(),
                loan.getUsers().getFirstName() + " " + loan.getUsers().getLastName()
            )).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Approve or reject loan
     * @param loanId
     * @param statusId
     * @return
     */
    @Override
    public GetLoanDto approveOrRejectLoan(Long loanId, Long statusId) {
        Optional<Loan> existingLoan = loanRepository.findById(loanId);
        if (existingLoan.isPresent()) {
            //Retrieve the loan status using the status id passed in the URL
            LoanStatus status = statusRepository.findById(statusId).orElseThrow(() -> new RuntimeException("Loan status not found"));
            existingLoan.get().setLoanStatus(status);
            Loan updatedLoan = loanRepository.save(existingLoan.get());
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
}