package com.acme.test01.tornikeshelia.model;

import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CurrentAccount extends Account {
    public static final BigDecimal MAX_OVERDRAFT = new BigDecimal(100000);
    private BigDecimal overdraftLimit;

    public CurrentAccount(Long id, String customerNumber, BigDecimal balance, BigDecimal overdraftLimit) {
        super(id, customerNumber, balance);
        if (overdraftLimit.compareTo(MAX_OVERDRAFT) > 0) {
            throw new RuntimeException("OverDraftLimit is more than allowed amount. Allowed limit : " + MAX_OVERDRAFT);
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) throws WithdrawalAmountTooLargeException {
        if (balance.add(overdraftLimit).subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new WithdrawalAmountTooLargeException();
        }
        this.balance = this.balance.subtract(amount);
    }
}
