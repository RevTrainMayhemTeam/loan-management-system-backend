package com.mayhem.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GetLoanByUserIdDto {
    private Long loanId;
    private Double amount;
    private Integer term;
    private String type;
    private String status;
    private Long userId;

    public GetLoanByUserIdDto() {
    }

    public GetLoanByUserIdDto(Long loanId, Double amount, Integer term, String type, String status, Long userId) {
        this.loanId = loanId;
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.status = status;
        this.userId = userId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
