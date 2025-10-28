package com.mxworld.mxworld.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.ModuleInterface;
import com.mxworld.mxworld.model.Course;
import com.mxworld.mxworld.repository.CourseRepository;
import com.mxworld.mxworld.repository.ModuleRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Module.Module;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModuleService implements ModuleInterface {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public ApiResponseDto<?> addModule(String courseId, Module moduleRequest) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (courseOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Course not found", null);
        }

        Course course = courseOpt.get();

        com.mxworld.mxworld.model.Module module = new com.mxworld.mxworld.model.Module();

        module.setModuleName(moduleRequest.getModuleName());
        module.setModuleDecription(moduleRequest.getModuleDescription());
        module.setCourse(course);

        moduleRepository.save(module);

        return new ApiResponseDto<>(201, "Module added successfully", null);
    }

    @Override
    public ApiResponseDto<?> getAllModule(String id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Course not found", null);
        }

        List<com.mxworld.mxworld.model.Module> modules = moduleRepository.findByCourseId(id);

        if (modules.isEmpty()) {
            return new ApiResponseDto<>(200, "No modules found for this course", modules);
        }

        return new ApiResponseDto<>(200, "Modules fetched successfully", modules);
    }

    @Override
    public ApiResponseDto<?> updateModule(String courseId, String moduleId , Module moduleRequest) {

        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (courseOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Course not found", null);
        }

        Optional<com.mxworld.mxworld.model.Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        com.mxworld.mxworld.model.Module module = moduleOpt.get();

        if (!module.getCourse().getId().equals(courseId)) {
            return new ApiResponseDto<>(404, "Module doesnt belong to this course", null);
        }

        module.setModuleName(moduleRequest.getModuleName());
        module.setModuleDecription(moduleRequest.getModuleDescription());
        moduleRepository.save(module);

         return new ApiResponseDto<>(200 , "Module updated successfully", null);
    }

    @Override
    public ApiResponseDto<?> deleteModule(String courseId, String moduleId) {

        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (courseOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Course not found", null);
        }

        Optional<com.mxworld.mxworld.model.Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return new ApiResponseDto<>(404, "Module not found", null);
        }

        com.mxworld.mxworld.model.Module module = moduleOpt.get();

        if (!module.getCourse().getId().equals(courseId)) {
            return new ApiResponseDto<>(404, "Module doesnt belong to this course", null);
        }

        moduleRepository.delete(module);

         return new ApiResponseDto<>(200 , "Module deleted successfully", null);
    }
}
