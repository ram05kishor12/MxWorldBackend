package com.mxworld.mxworld.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.interfaces.LectureInterface;
import com.mxworld.mxworld.repository.LectureRepository;
import com.mxworld.mxworld.repository.ModuleRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Lecture.Lecture;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@Tag(name = "Lecture Api")
@AllArgsConstructor
public class LectureController {
    
    private final LectureInterface lectureInterface;

    @PostMapping("/addLecture/{moduleId}")
    public ResponseEntity<ApiResponseDto<?>> addLecture(
            @RequestParam String moduleId,
            @RequestBody Lecture lectureRequest) {
        try {
            ApiResponseDto<?> response = lectureInterface.addLecture(moduleId, lectureRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    } 

    @GetMapping("/getAllLectures")
    public ResponseEntity<ApiResponseDto<?>> getAllLecture(@RequestParam String moduleId) {
        try {
            ApiResponseDto<?> respone = lectureInterface.getAllLecture(moduleId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }


}
