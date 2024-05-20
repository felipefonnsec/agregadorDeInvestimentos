package com.project.agregadorinvestimentos.controllers;

import com.project.agregadorinvestimentos.dtos.AccountStockResponseDto;
import com.project.agregadorinvestimentos.dtos.AssociateAccountStockDto;
import com.project.agregadorinvestimentos.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                            @RequestBody AssociateAccountStockDto dto){
        accountService.associateStock(accountId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable("accountId") String accountId){
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
