package com.mayhem.lms.service;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;

public interface AccountService {
    Account createAccount(RegisterDto newAccount);
    Account getAccountByEmail(String email);
    Boolean verifyCredentials(AuthDto userCredentials);
}
