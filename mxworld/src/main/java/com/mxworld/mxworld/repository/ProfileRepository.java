package com.mxworld.mxworld.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Profile;


public interface ProfileRepository extends JpaRepository<Profile , Long>{
   Optional<Profile> findById(Long id);
} 
