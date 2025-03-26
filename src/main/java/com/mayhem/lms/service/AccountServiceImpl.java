package com.mayhem.lms.service;

import com.mayhem.lms.dto.AuthDto;
import com.mayhem.lms.dto.RegisterDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.Account;
import com.mayhem.lms.model.AccountRole;
import com.mayhem.lms.model.User;
import com.mayhem.lms.repository.AccountRepository;
import com.mayhem.lms.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;
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
        //encrypt password with Bcrypt
        String hashPassword = BCrypt.hashpw(newAccount.getPassword(), BCrypt.gensalt());
        if (!validateEmail(newAccount.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        AccountRole role = roleRepository.findById(newAccount.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Account account = new Account();
        account.setEmail(newAccount.getEmail());
        account.setPassword(hashPassword);
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

    @Override
    public Account getAccountByEmail(String email) {
        Optional<Account> foundAccount = accountRepository.findByEmail(email);
        return foundAccount.orElse(null);
    }

    @Override
    public Boolean verifyCredentials(AuthDto userCredentials) {
        Optional<Account> account = accountRepository.findByEmail(userCredentials.getEmail());
        return account.isPresent() && BCrypt.checkpw(userCredentials.getPassword(), account.get().getPassword());
    }
}
