package com.mxworld.mxworld.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;




public interface ProfileRepository extends JpaRepository<Profile , Long>{
   Optional<Profile> findById(Long id);
   Optional<Profile> findByUser(User user);
} 
