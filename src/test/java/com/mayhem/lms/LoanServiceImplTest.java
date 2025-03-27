package com.mayhem.lms;

import com.mayhem.lms.dto.GetLoanDto;
import com.mayhem.lms.dto.GetUserDto;
import com.mayhem.lms.model.*;
import com.mayhem.lms.repository.LoanRepository;
import com.mayhem.lms.repository.UserRepository;
import com.mayhem.lms.service.LoanServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanServiceImpl;

    private Loan loanTest;
    public User userTest;
    public GetUserDto getUserDtoTest;
    private Account accountTest;
    private AccountRole accountRoleTest;
    //private LoanType loanTypeTest;
    //private LoanStatus loanStatusTest;

    @BeforeEach
    public void setUp() {
        loanTest = new Loan();

        loanTest.setId(3L);
        loanTest.setAmount(30000D);
        loanTest.setTerm(3);
        loanTest.setLoanTypes(new LoanType());
        loanTest.setLoanStatus(new LoanStatus());

        //loanTypeTest = new LoanType();
        //loanTypeTest.setId(1L);
        //loanTest.setLoanTypes(loanTypeTest);
        userTest = new User();
        userTest.setId(1L);
        userTest.setFirstName("John");
        userTest.setLastName("Smith");
        userTest.setAccount(new Account());
        loanTest.setUsers(userTest);



        accountRoleTest = new AccountRole();
        accountRoleTest.setRoleName("Manager");

        accountTest = new Account();
        accountTest.setId(1L);
        //accountTest.setEmail("manu.rios@example.com"); // It's good practice to set the email as well
        accountTest.setRole(accountRoleTest);

        userTest.setAccount(accountTest);

        getUserDtoTest = new GetUserDto();
        getUserDtoTest.setId(1L);
    }

    @Test
    public void testGetAllLoans() {
        when(loanRepository.findAll()).thenReturn(List.of(loanTest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));

        List <GetLoanDto> loans = loanServiceImpl.getAllLoans();

        assertNotEquals(3, loans.size());
        assertEquals(1, loans.size());
        assertEquals(3, loans.get(0).getId());

        verify(loanRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetLoanById() {

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loanTest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));

        GetLoanDto loanById = loanServiceImpl.getLoanById(1L, getUserDtoTest);

        assertEquals(3, loanById.getId());
    }


}