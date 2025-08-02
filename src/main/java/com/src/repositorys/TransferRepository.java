package com.src.repositorys;

import com.src.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    @Query("SELECT u FROM Transfer u WHERE u.senderAccount.id = :accountId")
    List<Transfer> getAllTransfersByAccountId(@Param("accountId") String accountId);

    @Query("SELECT u FROM Transfer u where u.sender.id = :userId")
    List<Transfer> getAllTransfersByUserId(@Param("userId") String userId);
}