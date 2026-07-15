package com.example.rabbit_config.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbit_config.Model.DB_Tables.UserToken;

public interface UserTokenRepo extends JpaRepository<UserToken, Long> {

    public Optional<UserToken> findByEmail(String email);
}