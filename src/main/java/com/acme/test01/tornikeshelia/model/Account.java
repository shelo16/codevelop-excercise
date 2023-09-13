package com.acme.test01.tornikeshelia.model;

import com.acme.test01.tornikeshelia.exception.WithdrawalAmountTooLargeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Account {

    protected Long id;
    protected String customerNumber;
    protected BigDecimal balance;

    public abstract void withdraw(BigDecimal amount) throws WithdrawalAmountTooLargeException;

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid Amount to deposit");
        }
        this.balance = this.balance.add(amount);
    }

}
