package com.mxworld.mxworld.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Otp;

public interface OtpRepository extends JpaRepository<Otp , String>{
    Optional<Otp> findByToken(String token);
    void deleteByToken(String token);
}
