package com.project.agregadorinvestimentos.repositories;

import com.project.agregadorinvestimentos.entity.Account;
import com.project.agregadorinvestimentos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, String> {
}
