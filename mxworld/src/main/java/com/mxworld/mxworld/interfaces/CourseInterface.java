package com.mxworld.mxworld.interfaces;

import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Course.Course;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface CourseInterface {
    ApiResponseDto<?> addCourses(@RequestBody Course courseRequest);
    ApiResponseDto<?> getAllCourse();
    ApiResponseDto<?> deleteCourse(String id);
    ApiResponseDto<?> updateCourse(String id , @RequestBody Course courseRequest);
}
