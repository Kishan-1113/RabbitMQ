package com.example.rabbit_config.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbit_config.Model.DB_Tables.UserTable;

public interface UserTableRepo extends JpaRepository<UserTable, Long> {

    public Optional<UserTable> findByEmail(String email);

}
