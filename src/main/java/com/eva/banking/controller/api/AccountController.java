package com.eva.banking.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.banking.dto.AccountRequest;
import com.eva.banking.dto.ApiResponse;
import com.eva.banking.dto.DepositRequest;
import com.eva.banking.exception.AccessDeniedException;
import com.eva.banking.exception.AccountNotFoundException;
import com.eva.banking.exception.InsufficientBalanceException;
import com.eva.banking.model.AccountEntity;
import com.eva.banking.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {

        try {
            List<AccountEntity> accounts = accountService.getAll();
            return ResponseEntity.ok(accounts);
        } catch (AccessDeniedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("ERROR", "Unknown error occurred"));
        }

    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest request) {

        try {
            AccountEntity newAccount = accountService.create(request);
            return ResponseEntity.ok(newAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("ERROR", "Unknown error occurred"));
        }
    }

    // get account by id, to get balance and other details
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    // deposit to account by id
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {

        try {
            AccountEntity account = accountService.depositToAccount(depositRequest);
            return ResponseEntity.ok(
                    new ApiResponse("SUCCESS", "Deposit completed", account));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("ERROR", e.getMessage()));
        } catch (AccountNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            // санамсаргүй өөр алдаа
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("ERROR", "Unknown error occurred"));
        }

    }

}
