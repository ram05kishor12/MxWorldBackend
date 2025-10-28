package com.mxworld.mxworld.interfaces;

import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Lecture.Lecture;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface LectureInterface {
    ApiResponseDto<?> addLecture(String moduleId ,  @RequestBody Lecture lectureRequest);
}
