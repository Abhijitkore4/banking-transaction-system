package com.abhijit.bankingsystem.repository;

import com.abhijit.bankingsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}