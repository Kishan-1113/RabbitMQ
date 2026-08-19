package com.example.rabbit_config.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbit_config.Model.DB_Tables.EmailTable;

public interface EmailTableRepo extends JpaRepository<EmailTable, Long> {

    public Optional<EmailTable> findByMessageId(String messgeId);

}
