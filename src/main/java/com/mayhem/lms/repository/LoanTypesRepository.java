package com.mayhem.lms.repository;

import com.mayhem.lms.model.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanTypesRepository extends JpaRepository<LoanType, Long> {
}
