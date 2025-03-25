package com.mayhem.lms.service;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;

public interface AccountService {
    public Account createAccount(RegisterDto newAccount);
    public Account getAccountByEmail(String email);
    public Boolean verifyCredentials(AuthDto userCredentials);
}
