package com.mayhem.lms.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account_role")
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true,  length = 50)
    private String roleName;

    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    public AccountRole() {
    }

    public AccountRole(Long id, String roleName, Set<Account> accounts) {
        this.id = id;
        this.roleName = roleName;
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
