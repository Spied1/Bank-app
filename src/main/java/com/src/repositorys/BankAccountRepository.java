package com.src.repositorys;

import com.src.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    @Query("SELECT u FROM BankAccount u where u.userId = :userId")
    List<BankAccount> getAllByUserId(@Param("userId") String userId);
}