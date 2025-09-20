package com.mxworld.mxworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mxworld.mxworld.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByEmail(String email);
}
