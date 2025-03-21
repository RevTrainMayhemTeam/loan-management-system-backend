package com.mayhem.lms.dto;

public class GetLoanDto {
    private Double amount;
    private Integer term;
    private String type;
    private String clientName;

    public GetLoanDto() {
    }

    public GetLoanDto(Double amount, Integer term, String type, String clientName) {
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.clientName = clientName;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
