package com.mayhem.lms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "term", nullable = false)
    private Integer term;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private LoanType loanTypes;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private LoanStatus loanStatus;

    public Loan() {
    }

    public Loan(Long id, Double amount, Integer term, User users, LoanType loanTypes, LoanStatus loanStatus) {
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.users = users;
        this.loanTypes = loanTypes;
        this.loanStatus = loanStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public LoanType getLoanTypes() {
        return loanTypes;
    }

    public void setLoanTypes(LoanType loanTypes) {
        this.loanTypes = loanTypes;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
}
