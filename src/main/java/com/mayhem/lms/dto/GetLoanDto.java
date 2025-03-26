package com.mayhem.lms.dto;

public class GetLoanDto {

    private Long id;
    private Double amount;
    private Integer term;
    private String type;
    private String status;
    private String clientName;

    public GetLoanDto() {
    }
    public GetLoanDto(Long id, Double amount, Integer term, String type, String status, String clientName) {
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.type = type;
        this.status = status;
        this.clientName = clientName;
    }

    public Long getId(){return id;}

    public void setId(Long id){ this.id = id;}

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
    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
