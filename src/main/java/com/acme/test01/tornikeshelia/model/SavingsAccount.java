package com.acme.test01.tornikeshelia.model;

import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal(1000);

    public SavingsAccount(Long id, String customerNumber, BigDecimal balance) {
        super(id, customerNumber, balance);
        if (balance.compareTo(MINIMUM_BALANCE) < 0) {
            throw new IllegalArgumentException("Initial deposit must be at least " + MINIMUM_BALANCE);
        }
    }

    @Override
    public void withdraw(BigDecimal amount) throws WithdrawalAmountTooLargeException {
        if (balance.subtract(amount).compareTo(MINIMUM_BALANCE) < 0) {
            throw new WithdrawalAmountTooLargeException();
        }
        this.balance = this.balance.subtract(amount);
    }
}
