package com.project.agregadorinvestimentos.repositories;

import com.project.agregadorinvestimentos.entity.Account;
import com.project.agregadorinvestimentos.entity.AccountStock;
import com.project.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
