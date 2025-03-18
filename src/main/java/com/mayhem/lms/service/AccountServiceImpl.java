package com.mayhem.lms.service;

import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.repository.AccountRepository;
import com.mayhem.lms.repository.RoleRepository;
import org.springframework.stereotype.Service;

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
        Account account = new Account();
        account.setEmail(newAccount.getEmail());
        account.setPassword(newAccount.getPassword());
        account.setRole(role);

        return accountRepository.save(account);
    }
}
