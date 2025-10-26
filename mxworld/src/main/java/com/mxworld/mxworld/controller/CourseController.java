package com.mxworld.mxworld.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.interfaces.CourseInterface;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Course.Course;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/mxworld/v1/course")
@Tag(name = "Course API")
public class CourseController {

    private final CourseInterface courseInterface;

    public CourseController(CourseInterface courseInterface) {
        this.courseInterface = courseInterface;
    }

    @PostMapping("/addCourse")
    public ResponseEntity<ApiResponseDto<?>> addCourse(@RequestBody Course courseRequest) {
        try {
            if (courseRequest.getBase64() == null || courseRequest.getCourseName() == null
                    || courseRequest.getCourseName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDto<>(400, "Invalid Payload", null));
            }
            ApiResponseDto<?> respone = courseInterface.addCourses(courseRequest);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

    @GetMapping("/getAllCourse")
    public ResponseEntity<ApiResponseDto<?>> getAllCourse() {
        try {
            ApiResponseDto<?> course = courseInterface.getAllCourse();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteCourse(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDto<>(400, "Invalid Payload", null));
            }
            ApiResponseDto<?> response = courseInterface.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

    @PutMapping("/updateCourse/{id}")
    public ResponseEntity<ApiResponseDto<?>> updateCourse(@PathVariable String id, @RequestBody Course courseRequest) {
        try {
           ApiResponseDto<?> course = courseInterface.updateCourse(id, courseRequest);
           return ResponseEntity.status(HttpStatus.OK).body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }
}
