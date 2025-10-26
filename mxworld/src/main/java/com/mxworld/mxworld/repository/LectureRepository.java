package com.mxworld.mxworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture , String>{
    
}
