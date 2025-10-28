package com.mxworld.mxworld.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Lecture;


public interface LectureRepository extends JpaRepository<Lecture , String>{
    List<Lecture> findByModuleId(String moduleId);
}
