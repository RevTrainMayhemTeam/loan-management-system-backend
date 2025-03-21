package com.mayhem.lms.dto;

public class GetLoanDto {
    private Long loanId;
    private Float amount;
    private Integer term;
    private String type;
    private String clientName;

    public GetLoanDto() {
    }

    public GetLoanDto(Long loanId, Float amount, Integer term, String type, String clientName) {
        this.loanId = loanId;
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.clientName = clientName;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
