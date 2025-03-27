package com.mayhem.lms.dto;

import com.mayhem.lms.model.LoanStatus;
import com.mayhem.lms.model.LoanType;

public class UpdateLoanDto {
    private Double amount;
    private Integer term;
    private LoanType type;
    private LoanStatus status;

    public UpdateLoanDto() {
    }

    public UpdateLoanDto(Double amount, Integer term, LoanType type, LoanStatus status) {
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}