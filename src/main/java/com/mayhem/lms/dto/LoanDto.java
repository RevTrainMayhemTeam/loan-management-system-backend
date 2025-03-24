package com.mayhem.lms.dto;

public class LoanDto {
    private Long id;
    private Float amount;
    private Integer term;
    private String type;
    private String Status;
    private String clientName;

    public LoanDto() {
    }

    public LoanDto(Long id, Float amount, Integer term, String type, String status, String clientName) {
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.type = type;
        Status = status;
        this.clientName = clientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
