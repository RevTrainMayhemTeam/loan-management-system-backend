package com.mayhem.lms.service;

import com.mayhem.lms.dto.CreateLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.LoanStatus;
import com.mayhem.lms.model.LoanType;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.model.Loan;
import com.mayhem.lms.repository.LoanStatusRepository;
import com.mayhem.lms.repository.LoanTypeRepository;
import com.mayhem.lms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService{

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

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
    public GetLoanDto updateLoan(Long id, Loan loanDetails, GetUserDto session) {
        Loan existingLoan = loanRepository.findById(id).orElse(null);
        if (existingLoan.getUsers().getId() != session.getId()) {
            logger.error("Unauthorized access attempt for userId: {} to update loan with id: {}", session.getId(), id);
            return null;
        }

        if (existingLoan != null) {
            existingLoan.setAmount(loanDetails.getAmount());
            existingLoan.setTerm(loanDetails.getTerm());

            // Get loanType by id
            LoanType loanType = typeRepository.findById(loanDetails.getLoanTypes().getId())
                    .orElseThrow(() -> new RuntimeException("Loan type not found"));
            existingLoan.setLoanTypes(loanType);

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
        logger.info("Loan created successfully for userId: {}", user.getId());
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
            logger.info("Loans found for userId {}", userId);
            return loans.get().stream().map(loan -> new GetLoanDto(
                    loan.getId(),
                    loan.getAmount(),
                    loan.getTerm(),
                    loan.getLoanTypes().getType(),
                    loan.getLoanStatus().getStatus(),
                    loan.getUsers().getFirstName() + " " + loan.getUsers().getLastName()
            )).collect(Collectors.toList());
        }
        logger.info("No loans found for userId {}", userId);
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
            logger.info("Loan status updated for loanId {} with statusId: {}", loanId, statusId);
            return new GetLoanDto(
                    updatedLoan.getId(),
                    updatedLoan.getAmount(),
                    updatedLoan.getTerm(),
                    updatedLoan.getLoanTypes().getType(),
                    updatedLoan.getLoanStatus().getStatus(),
                    updatedLoan.getUsers().getFirstName() + " " + updatedLoan.getUsers().getLastName()
            );
        }
        logger.info("No loan found with loanId {}", loanId);
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

        if(foundedLoan == null)
            return null;

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

        if (loanToDelete.getLoanStatus().getStatus().equals("Approved")) {
            logger.error("Loan with id {} cannot be deleted because it is already approved", loanId);
            return false;
        }

        if(userLogged.getId().equals(ownerId)){
            logger.info("Loan with id {} deleted successfully", loanId);
            loanRepository.delete(loanToDelete);
            return true;
        }
        return false;
    }
}