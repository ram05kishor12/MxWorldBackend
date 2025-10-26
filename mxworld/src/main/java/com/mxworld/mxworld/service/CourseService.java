package com.mxworld.mxworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.CourseInterface;
import com.mxworld.mxworld.model.Course;
import com.mxworld.mxworld.repository.CourseRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Course.CourseResponse;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService implements CourseInterface {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AttachmentService attachmentService; // ✅ to handle thumbnail uploads

    @Override
    public ApiResponseDto<?> addCourses(com.mxworld.mxworld.syntax.Course.Course courseRequest) {
        try {

            Course course = new Course();

            if (courseRequest.getBase64() != null && courseRequest.getBase64().startsWith("data:")) {
                ApiResponseDto<Map<String, String>> uploadResponse = attachmentService
                        .uploadFile(courseRequest.getBase64());

                // ApiResponseDto doesn't expose getStatusCode(); consider success when data is
                // present.
                if (uploadResponse.getData() != null) {
                    String fileUrl = uploadResponse.getData().get("fileUrl");
                    course.setThumbNail(fileUrl);
                    ;
                } else {
                    return new ApiResponseDto<>(400, "Thumbnail upload failed", null);
                }
            }

            course.setCourseName(courseRequest.getCourseName());
            course.setCourseDescription(courseRequest.getCourseDescription());

            Course savedCourse = courseRepository.save(course);

            return new ApiResponseDto<>(HttpStatus.CREATED.value(), "Course added successfully", null);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error adding course: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponseDto<?> getAllCourse() {
        try {
            List<CourseResponse> courses = courseRepository.findAll().stream()
                    .map(course -> new CourseResponse(
                            course.getId(),
                            course.getCourseName(),
                            course.getCourseDescription(),
                            course.getThumbNail()))
                    .collect(Collectors.toList());

            return new ApiResponseDto<>(200, "Courses fetched successfully", courses);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error adding course: " + e.getMessage(), null);
        }

    }

    @Override
    public ApiResponseDto<?> deleteCourse(String id) {
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isEmpty()) {
                return new ApiResponseDto<>(404, "Courses Not Found", null);
            }
            courseRepository.delete(course.get());
            return new ApiResponseDto<>(200, "Course deleted successfully", null);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error adding course: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponseDto<?> updateCourse(String id, com.mxworld.mxworld.syntax.Course.Course courseRequest) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(id);
            if (courseOpt.isEmpty()) {
                return new ApiResponseDto<>(404, "Course Not Found", null);
            }

            Course course = courseOpt.get();

            if (courseRequest.getBase64() != null && courseRequest.getBase64().startsWith("data:")) {
                ApiResponseDto<Map<String, String>> uploadResponse = attachmentService
                        .uploadFile(courseRequest.getBase64());

                if (uploadResponse.getData() != null) {
                    String fileUrl = uploadResponse.getData().get("fileUrl");
                    course.setThumbNail(fileUrl);
                } else {
                    return new ApiResponseDto<>(400, "Thumbnail upload failed", null);
                }
            }

            course.setCourseName(courseRequest.getCourseName());
            course.setCourseDescription(courseRequest.getCourseDescription());

            courseRepository.save(course);

            return new ApiResponseDto<>(200, "Course updated successfully", null);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error updating course: " + e.getMessage(), null);
        }
    }
}
