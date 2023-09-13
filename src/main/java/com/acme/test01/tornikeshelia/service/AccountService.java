package com.acme.test01.tornikeshelia.service;

import com.acme.test01.tornikeshelia.exception.AccountNotFoundException;
import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;

import java.math.BigDecimal;

public interface AccountService {

    void openSavingsAccount(Long accountId, BigDecimal amountToDeposit);
    void openCurrentAccount(Long accountId, BigDecimal overDraftLimit);
    void withdraw(Long accountId, BigDecimal amountToWithdraw) throws WithdrawalAmountTooLargeException;
    void deposit(Long accountId, BigDecimal amountToDeposit) throws AccountNotFoundException;

}
