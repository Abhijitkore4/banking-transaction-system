package com.abhijit.bankingsystem.repository;

import com.abhijit.bankingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}