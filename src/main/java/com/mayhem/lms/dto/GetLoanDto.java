package com.mayhem.lms.dto;

public class GetLoanDto {
    private Float amount;
    private Integer term;
    private String type;
    private String clientName;

    public GetLoanDto() {
    }

    public GetLoanDto(Float amount, Integer term, String type, String clientName) {
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.clientName = clientName;
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
