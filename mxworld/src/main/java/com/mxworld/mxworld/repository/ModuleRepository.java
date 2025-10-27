package com.mxworld.mxworld.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Module;

import jakarta.persistence.Id;



public interface ModuleRepository extends JpaRepository<com.mxworld.mxworld.model.Module , String>{
    Optional<Module> findById(Id id);
    List<Module> findByCourseId(String courseId);
}
