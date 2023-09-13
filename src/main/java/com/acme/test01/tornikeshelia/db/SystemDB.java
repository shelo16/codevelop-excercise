package com.acme.test01.tornikeshelia.db;

import com.acme.test01.tornikeshelia.model.Account;
import com.acme.test01.tornikeshelia.model.CurrentAccount;
import com.acme.test01.tornikeshelia.model.SavingsAccount;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SystemDB {

    private static SystemDB instance = null;

    private Map<Long, Account> accounts;

    private SystemDB() {
        accounts = new HashMap<>();
        accounts.put(1L, new SavingsAccount(1L, "1", new BigDecimal(2000)));
        accounts.put(2L, new SavingsAccount(2L, "2", new BigDecimal(5000)));
        accounts.put(3L, new CurrentAccount(3L, "3", new BigDecimal(1000), new BigDecimal(10000)));
        accounts.put(4L, new CurrentAccount(4L, "4", new BigDecimal(-5000), new BigDecimal(20000)));
    }

    public Account getAccount(Long id) {
        return accounts.get(id);
    }
}

