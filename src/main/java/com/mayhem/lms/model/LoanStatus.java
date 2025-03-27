package com.mayhem.lms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_status")
public class LoanStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @Column(name = "status", nullable = false, unique = true, length = 50)
    private String status;

    @OneToMany(mappedBy = "loanStatus", cascade = CascadeType.ALL)
    @JsonManagedReference("status-loan")
    private List<Loan> loans = new ArrayList<>();

    public LoanStatus() {
    }

    public LoanStatus(Long id, String status, List<Loan> loans) {
        this.id = id;
        this.status = status;
        this.loans = loans;
    }

    public LoanStatus(Long id, String status) {
        this.status = status;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    @Override
    public String toString() {
        return "LoanStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
