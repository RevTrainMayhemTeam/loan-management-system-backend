package com.mayhem.lms.service;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.dto.UserDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.AccountRepository;
import com.mayhem.lms.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Account getAccountByEmail(String email) {
        Optional<Account> foundAccount = accountRepository.findByEmail(email);
        return foundAccount.orElse(null);
    }

    @Override
    public Boolean verifyCredentials(AuthDto userCredentials) {
        Optional<Account> account = accountRepository.findByEmail(userCredentials.getEmail());
        return account.isPresent() && account.get().getPassword().equals(userCredentials.getPassword());
    }
}
