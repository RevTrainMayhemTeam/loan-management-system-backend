package com.mayhem.lms.service;

import com.mayhem.lms.model.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findAccountById(Long id);
}
