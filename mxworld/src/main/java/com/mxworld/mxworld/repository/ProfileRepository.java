package com.mxworld.mxworld.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;

import jakarta.persistence.Id;




public interface ProfileRepository extends JpaRepository<Profile , String>{
   Optional<Profile> findById(Id id);
   Optional<Profile> findByUser(User user);
} 
