package com.eva.banking.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eva.banking.dto.AccountRequest;
import com.eva.banking.dto.DepositRequest;
import com.eva.banking.exception.AccountNotFoundException;
import com.eva.banking.model.AccountEntity;
import com.eva.banking.model.TransactionEntity;
import com.eva.banking.model.UserEntity;
import com.eva.banking.repository.AccountRepository;
import com.eva.banking.repository.TransactionRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository,
            UserService userService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    // get all accounts for the current logged-in user
    public List<AccountEntity> getAll() {

        UserEntity currentUser = userService.getCurrentUser();

        return accountRepository.findAllAccountsByUserId(currentUser.getId());
    }

    public AccountEntity create(AccountRequest request) {

        if (request.getInitialDeposit() < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }

        if (request.getAccountNo() == null || request.getAccountNo().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }

        if (request.getAccountType() == null || request.getAccountType().toString().isEmpty()) {
            throw new IllegalArgumentException("Account type is required");
        }

        AccountEntity newAccount = new AccountEntity();
        newAccount.setAccountNo(request.getAccountNo());
        newAccount.setAccountType(request.getAccountType());
        newAccount.setBalance(request.getInitialDeposit());

        // private String accountNo;
        // private AccountType accountType;
        // private Double initialDeposit;

        return new AccountEntity();
    }

    // create an account for the current logged-in user
    public AccountEntity save(AccountEntity account) {
        UserEntity currentUser = userService.getCurrentUser();
        account.setUserId(currentUser.getId());
        return accountRepository.save(account);
    }

    public AccountEntity findById(Long id) {

        try {
            Long userId = userService.getCurrentUser().getId();
            return accountRepository.findAccountByIdAndUserId(id, userId);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving account", e);
        }

        // return accountRepository.findAccountByIdAndUserId(id,
        // userService.getCurrentUser().getId())
        // .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Transactional
    public AccountEntity depositToAccount(DepositRequest depositRequest) {

        if (depositRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        System.out.println("Starting deposit of " + depositRequest.getAmount() +
                " to account ID " + depositRequest.getAccountId());

        // 1. Данс олно
        AccountEntity account = findById(depositRequest.getAccountId());

        System.out.println("Account found: " + account);

        // 2. Балансыг нэмнэ
        account.setBalance(account.getBalance() + depositRequest.getAmount());

        System.out.println("New balance: " + account.getBalance());

        // 3. Transaction бичнэ
        TransactionEntity tx = new TransactionEntity();
        // tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(depositRequest.getAmount());
        tx.setToAccount(account); // данс руу орж байгаа
        tx.setFromAccount(null); // cash-ээс орж байгаа гэж үзээд null үлдээнэ
        tx.setCreatedAt(Instant.now());

        System.out.println("Transaction created: " + tx);

        // 4. Хадгална
        transactionRepository.save(tx);

        System.out.println("Transaction saved: " + tx);

        accountRepository.save(account);

        System.out.println("Account updated: " + account);

        return account;

    }

    // Business logic related to accounts goes here
}
