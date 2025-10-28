package com.mxworld.mxworld.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.LectureInterface;
import com.mxworld.mxworld.model.Course;
import com.mxworld.mxworld.repository.LectureRepository;
import com.mxworld.mxworld.repository.ModuleRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Lecture.Lecture;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LectureService implements LectureInterface{

    private final ModuleRepository moduleRepository;
    private final LectureRepository LectureRepository;
    
    @Override
    public ApiResponseDto<?> addLecture(String moduleId , @RequestBody Lecture lectureRequest) {
        Optional<com.mxworld.mxworld.model.Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        com.mxworld.mxworld.model.Module module = moduleOpt.get();

        com.mxworld.mxworld.model.Lecture lecture = new com.mxworld.mxworld.model.Lecture();

        lecture.setLectureName(lectureRequest.getLectureName());
        lecture.setLectureContent(lectureRequest.getLectureContent());
        lecture.setModule(module);
        
        LectureRepository.save(lecture);

        return new ApiResponseDto<>(201, "Module added successfully", null);
    }
}
