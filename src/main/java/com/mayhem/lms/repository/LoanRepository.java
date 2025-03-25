package com.mayhem.lms.repository;

import com.mayhem.lms.model.Loan;
import com.mayhem.lms.model.LoanStatus;
import com.mayhem.lms.model.LoanType;
import com.mayhem.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{
}
