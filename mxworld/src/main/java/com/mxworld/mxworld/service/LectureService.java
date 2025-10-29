package com.mxworld.mxworld.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.LectureInterface;
import com.mxworld.mxworld.model.Course;
import com.mxworld.mxworld.model.Module;
import com.mxworld.mxworld.repository.LectureRepository;
import com.mxworld.mxworld.repository.ModuleRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Lecture.Lecture;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LectureService implements LectureInterface {

    private final ModuleRepository moduleRepository;
    private final LectureRepository lectureRepository;

    @Override
    public ApiResponseDto<?> addLecture(String moduleId, @RequestBody Lecture lectureRequest) {
        Optional<com.mxworld.mxworld.model.Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        com.mxworld.mxworld.model.Module module = moduleOpt.get();

        com.mxworld.mxworld.model.Lecture lecture = new com.mxworld.mxworld.model.Lecture();

        lecture.setLectureName(lectureRequest.getLectureName());
        lecture.setLectureContent(lectureRequest.getLectureContent());
        lecture.setModule(module);

        lectureRepository.save(lecture);

        return new ApiResponseDto<>(201, "Module added successfully", null);
    }

    @Override
    public ApiResponseDto<?> getAllLecture(String moduleId) {
        Optional<Module> moduleOtp = moduleRepository.findById(moduleId);

        if (moduleOtp.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        List<com.mxworld.mxworld.model.Lecture> lectures = lectureRepository.findByModuleId(moduleId);

        if (lectures.isEmpty()) {
            return new ApiResponseDto<>(200, "No lectures found for this module", null);
        }

        return new ApiResponseDto<>(200, "Lectures fetched successfully", lectures);
    }

    @Override
    public ApiResponseDto<?> updateLecture(String moduleId, String lectureId, Lecture lectureRRequest) {

        Optional<Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        Optional<com.mxworld.mxworld.model.Lecture> lectureOpt = lectureRepository.findById(lectureId);

        if (lectureOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Lecture not found", null);
        }

        com.mxworld.mxworld.model.Lecture lecture = lectureOpt.get();

        if (!lecture.getModule().getId().equals(moduleId)) {
            return new ApiResponseDto<>(404, "Lecture doesnt belong to this module", null);
        }

        lecture.setLectureName(lectureRRequest.getLectureName());
        lecture.setLectureContent(lectureRRequest.getLectureContent());
        lectureRepository.save(lecture);

        return new ApiResponseDto<>(200, "Lecture updated successfully", null);
    }
}
