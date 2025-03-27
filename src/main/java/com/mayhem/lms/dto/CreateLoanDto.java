package com.mayhem.lms.dto;

public class CreateLoanDto {
    private Long loanId;
    private Double amount;
    private Integer term;
    private Long userId;
    private Long type;
    private Long statusId;

    public CreateLoanDto() {
    }

    public CreateLoanDto(Long loanId, Double amount, Integer term, Long userId, Long type, Long statusId) {
        this.loanId = loanId;
        this.amount = amount;
        this.term = term;
        this.userId = userId;
        this.type = type;
        this.statusId = 1L;
    }

    public CreateLoanDto(Long loanId, Double amount, Integer term, Long userId, Long type) {
        this.loanId = loanId;
        this.amount = amount;
        this.term = term;
        this.userId = userId;
        this.type = type;
        this.statusId = 1L;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
