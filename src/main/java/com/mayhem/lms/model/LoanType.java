package com.mayhem.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_type")
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    @Column(name = "type", nullable = false, unique = true, length = 50)
    private String type;

    @OneToMany(mappedBy = "loanTypes", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();
}
