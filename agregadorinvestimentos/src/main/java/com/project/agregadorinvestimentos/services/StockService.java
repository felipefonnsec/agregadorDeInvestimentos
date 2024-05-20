package com.project.agregadorinvestimentos.services;

import com.project.agregadorinvestimentos.dtos.CreateStockDto;
import com.project.agregadorinvestimentos.entity.Stock;
import com.project.agregadorinvestimentos.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    StockRepository stockRepository;

    public void createStock(CreateStockDto createStockDto) {
        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );
        stockRepository.save(stock);
    }
}
