package com.acme.test01.tornikeshelia.service;

import com.acme.test01.tornikeshelia.db.SystemDB;
import com.acme.test01.tornikeshelia.exception.AccountNotFoundException;
import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;
import com.acme.test01.tornikeshelia.model.Account;
import com.acme.test01.tornikeshelia.model.CurrentAccount;
import com.acme.test01.tornikeshelia.model.SavingsAccount;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class AccountServiceImpl implements AccountService {
    private final SystemDB systemDB;

    public AccountServiceImpl(SystemDB systemDB) {
        this.systemDB = systemDB;
    }

    @Override
    public void openSavingsAccount(Long accountId, BigDecimal amountToDeposit) {

        log.info("Opening Savings account");
        SavingsAccount account = new SavingsAccount(accountId, accountId.toString(), amountToDeposit);
        systemDB.getAccounts().put(accountId, account);
        log.info("Savings account opened successfully with id : " + accountId);
    }

    @Override
    public void openCurrentAccount(Long accountId, BigDecimal overDraftLimit) {
        log.info("Opening Current account");
        CurrentAccount account = new CurrentAccount(accountId, accountId.toString(), BigDecimal.ZERO, overDraftLimit);
        systemDB.getAccounts().put(accountId, account);
        log.info("Current account opened successfully with id : " + accountId);
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amountToWithdraw)
            throws AccountNotFoundException, WithdrawalAmountTooLargeException {
        Account account = systemDB.getAccount(accountId);
        if (account == null) {
            log.error("Error during withdrawal, account id : " + accountId + " AmountToWithdraw : " + amountToWithdraw);
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        if (account instanceof CurrentAccount currAccount) {
            BigDecimal totalAvailable = currAccount.getBalance().add(currAccount.getOverdraftLimit());
            if (amountToWithdraw.compareTo(totalAvailable) > 0) {
                log.error("Error during withdrawal currentAccount : AmountToWithdraw too large : " + amountToWithdraw);
                throw new WithdrawalAmountTooLargeException();
            }
        }
        account.withdraw(amountToWithdraw);
        log.info("Amount withdrawn successfully");
    }

    @Override
    public void deposit(Long accountId, BigDecimal amountToDeposit) throws AccountNotFoundException {
        Account account = systemDB.getAccount(accountId);
        if (account == null) {
            log.error("Error during deposit - AccountId : " + accountId + " AmountToDeposit : " + amountToDeposit);
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        account.deposit(amountToDeposit);
        log.info("Successfully made a deposit");
    }
}



