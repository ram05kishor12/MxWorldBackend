package com.mxworld.mxworld.interfaces;

import java.util.Optional;

import com.mxworld.mxworld.model.RefreshToken;
import com.mxworld.mxworld.model.User;

public interface RefreshTokenInterface {
    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
}
