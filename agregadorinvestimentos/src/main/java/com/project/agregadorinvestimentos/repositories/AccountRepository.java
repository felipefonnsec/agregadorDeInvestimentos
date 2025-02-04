package com.project.agregadorinvestimentos.repositories;

import com.project.agregadorinvestimentos.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
