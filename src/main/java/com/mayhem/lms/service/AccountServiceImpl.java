package com.mayhem.lms.service;

import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.repository.AccountRepository;
import com.mayhem.lms.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Account createAccount(RegisterDto newAccount) {
        AccountRole role = roleRepository.getByRoleName("Customer");
//        AccountRole role = new AccountRole();
        Account account = new Account();
        account.setEmail(newAccount.getEmail());
        account.setPassword(newAccount.getPassword());
        account.setRole(role);

        return accountRepository.save(account);
    }

    /**
     *
     * @param email user email
     * @return true if email matches regex, false otherwise
     */
    public boolean validateEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}