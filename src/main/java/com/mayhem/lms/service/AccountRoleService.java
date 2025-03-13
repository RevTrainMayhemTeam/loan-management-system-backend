package com.mayhem.lms.service;

import com.mayhem.lms.model.AccountRole;

import java.util.Optional;

public interface AccountRoleService {
    Optional<AccountRole> findAccountRoleById(Long id);
}
