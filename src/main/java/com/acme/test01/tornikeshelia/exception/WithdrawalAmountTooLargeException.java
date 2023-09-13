package com.acme.test01.tornikeshelia.exception;

public class WithdrawalAmountTooLargeException extends Exception {

    public WithdrawalAmountTooLargeException() {
        super("Withdrawal amount exceeds the allowed limit.");
    }

}
