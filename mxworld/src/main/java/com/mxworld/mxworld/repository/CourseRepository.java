package com.mxworld.mxworld.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Course;
import com.mxworld.mxworld.model.Profile;

import jakarta.persistence.Id;

public interface CourseRepository extends JpaRepository<Course , String> {
    Optional<Profile> findById(Id id);
}
