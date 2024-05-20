package com.project.agregadorinvestimentos.services;

import com.project.agregadorinvestimentos.client.BrapClient;
import com.project.agregadorinvestimentos.dtos.AccountStockResponseDto;
import com.project.agregadorinvestimentos.dtos.AssociateAccountStockDto;
import com.project.agregadorinvestimentos.entity.AccountStock;
import com.project.agregadorinvestimentos.entity.AccountStockId;
import com.project.agregadorinvestimentos.repositories.AccountRepository;
import com.project.agregadorinvestimentos.repositories.AccountStockRepository;
import com.project.agregadorinvestimentos.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    AccountStockRepository accountStockRepository;
    @Autowired
    BrapClient brapClient;

    //associacao de stock
    public void associateStock(String accountId, AssociateAccountStockDto dto) {

        //verificar se existem no banco de dados
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //fazendo o relacionante e conversao DTO -> ENTITY
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );
        accountStockRepository.save(entity);
    }

    //fazendo a lista de stock
    public List<AccountStockResponseDto> listStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return
                account.getAccountStocks().stream()
                .map(as -> new AccountStockResponseDto(
                            as.getStock().getStockId(),
                            as.getQuantity(),
                            getTotal(as.getQuantity(),
                                     as.getStock().getStockId())
                    ))
                .toList();
    }

    //verificando o preço por quantidade e retornando a soma
    private double getTotal(Integer quantity, String stockId) {
        var response = brapClient.getQuote(TOKEN, stockId);

        var price = response.results().getFirst().regularMarketPrice();

        return quantity * price;
    }
}
