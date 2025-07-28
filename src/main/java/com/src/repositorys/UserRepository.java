package com.src.repositorys;

import com.src.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u.name FROM User u WHERE u.name = :name")
    List<String> findAllByName(@Param("name") String name);

    //maybeerror1900
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}