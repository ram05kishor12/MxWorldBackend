package com.mxworld.mxworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.RefreshTokenInterface;
import com.mxworld.mxworld.model.RefreshToken;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenInterface {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // Delete old tokens for the same user (optional, for security)
        refreshTokenRepository.deleteByEmail(user.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(user.getEmail());
        refreshToken.setToken(UUID.randomUUID().toString()); // Can also use JWT
        refreshToken.setExpiryDate(Instant.now().plusSeconds(86400)); // 1 day expiry
        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Override
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }
}
