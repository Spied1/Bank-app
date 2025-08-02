package com.src.repositorys;

import com.src.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT u FROM Account u where u.id = :userId")
    List<Account> getAllByUserId(@Param("userId") String userId);
}