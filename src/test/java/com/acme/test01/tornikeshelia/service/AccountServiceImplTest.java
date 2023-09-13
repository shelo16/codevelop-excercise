package com.acme.test01.tornikeshelia.service;

import com.acme.test01.tornikeshelia.db.SystemDB;
import com.acme.test01.tornikeshelia.exception.AccountNotFoundException;
import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;
import com.acme.test01.tornikeshelia.model.Account;
import com.acme.test01.tornikeshelia.model.CurrentAccount;
import com.acme.test01.tornikeshelia.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    private SystemDB systemDBMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOpenSavingsAccount() {
        //Setup
        ArgumentCaptor<Long> keyCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Account> valueCaptor = ArgumentCaptor.forClass(Account.class);

        Map<Long, Account> mockMap = mock(Map.class);

        // When
        when(systemDBMock.getAccounts()).thenReturn(mockMap);


        service.openSavingsAccount(456L, BigDecimal.valueOf(1000));

        // Then
        verify(systemDBMock, times(1)).getAccounts();

        verify(mockMap).put(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(456L, keyCaptor.getValue().longValue());
        assertTrue(valueCaptor.getValue() instanceof SavingsAccount);
    }


    @Test
    void testOpenCurrentAccount() {
        // Setup
        ArgumentCaptor<Long> keyCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Account> valueCaptor = ArgumentCaptor.forClass(Account.class);

        Map<Long, Account> mockMap = mock(Map.class);

        // When
        when(systemDBMock.getAccounts()).thenReturn(mockMap);

        service.openCurrentAccount(456L, new BigDecimal(50000));

        // Then
        verify(systemDBMock, times(1)).getAccounts();

        verify(mockMap).put(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(456L, keyCaptor.getValue().longValue());
        assertTrue(valueCaptor.getValue() instanceof CurrentAccount);
    }

    @Test
    void testOpenCurrentAccountInvalidOverDraftLimit() {

        // Then
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            // When
            service.openCurrentAccount(456L, new BigDecimal(150000));
        });

        assertTrue(thrownException.getMessage().contains("OverDraftLimit is more than allowed amount. Allowed limit : " + CurrentAccount.MAX_OVERDRAFT));

        verifyNoMoreInteractions(systemDBMock);
    }

    @Test
    void testWithdrawSuccess() throws Exception {

        // When
        when(systemDBMock.getAccount(789L)).thenReturn(new SavingsAccount(789L, "789", BigDecimal.valueOf(5000)));
        service.withdraw(789L, BigDecimal.valueOf(2000));

        // Then
        verify(systemDBMock, times(1)).getAccount(789L);
    }

    @Test
    void testWithdrawAccountNotFound() {

        // When
        when(systemDBMock.getAccount(999L)).thenReturn(null);

        // Then
        assertThrows(AccountNotFoundException.class, () -> {
            service.withdraw(999L, BigDecimal.valueOf(2000));
        });
    }

    @Test
    void testWithdrawAmountTooLarge() {

        // When
        when(systemDBMock.getAccount(1001L)).thenReturn(new CurrentAccount(1001L, "1001", BigDecimal.ZERO, BigDecimal.ZERO));

        // Then
        assertThrows(WithdrawalAmountTooLargeException.class, () -> {
            service.withdraw(1001L, BigDecimal.valueOf(200000));
        });
    }

    @Test
    void testDepositSuccess() throws Exception {

        // When
        when(systemDBMock.getAccount(1101L)).thenReturn(new SavingsAccount(1101L, "1101", BigDecimal.valueOf(3000)));
        service.deposit(1101L, BigDecimal.valueOf(2000));

        // Then
        verify(systemDBMock, times(1)).getAccount(1101L);
    }

    @Test
    void testDepositAccountNotFound() {

        // When
        when(systemDBMock.getAccount(1201L)).thenReturn(null);

        // Then
        assertThrows(AccountNotFoundException.class, () -> {
            service.deposit(1201L, BigDecimal.valueOf(2000));
        });
    }
}

