package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return account != null && accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return account != null && accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (fromId == toId || amount <= 0) {
            return false;
        }
        Account accountToTransfer = accounts.get(toId);
        Account accountFromTransfer = accounts.get(fromId);
        if (accountToTransfer != null && accountFromTransfer != null && !(accountFromTransfer.amount() < amount)) {
            accounts.put(accountToTransfer.id(),
                    new Account(accountToTransfer.id(), accountToTransfer.amount() + amount));
            accounts.put(accountFromTransfer.id(),
                    new Account(accountFromTransfer.id(), accountFromTransfer.amount() - amount));
        }
        return false;
    }
}

